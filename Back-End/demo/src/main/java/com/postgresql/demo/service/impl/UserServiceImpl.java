package com.postgresql.demo.service.impl;

import com.postgresql.demo.entity.User;
import com.postgresql.demo.exception.BadRequestException;
import com.postgresql.demo.modal.*;
import com.postgresql.demo.repository.UserRepo;
import com.postgresql.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "name",
            "username",
            "email"
    );

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(AddUserModal addUserModal) {
        validateNewUser(addUserModal);

        User user = new User();
        user.setName(addUserModal.getName());
        user.setEmail(addUserModal.getEmail());
        user.setUsername(addUserModal.getUsername());
        user.setEncryptPassword(
                passwordEncoder.encode(addUserModal.getPassword())
        );

        userRepo.save(user);
    }

    @Override
    @Transactional
    public User updateUser(UpdateUserModal updateUserModal) {
        User user = getUserOrThrow(updateUserModal.getUsername());

        boolean nameChanged = !Objects.equals(
                user.getName(),
                updateUserModal.getName()
        );

        boolean emailChanged = !Objects.equals(
                user.getEmail(),
                updateUserModal.getEmail()
        );

        if (emailChanged &&
                userRepo.existsByEmail(updateUserModal.getEmail())) {
            throw new BadRequestException(
                    ResponseModal.EMAIL_ALREADY_EXIST
            );
        }

        if (!nameChanged && !emailChanged) {
            return user;
        }

        if (nameChanged) {
            user.setName(updateUserModal.getName());
        }

        if (emailChanged) {
            user.setEmail(updateUserModal.getEmail());
        }

        return userRepo.save(user);
    }

    @Override
    public User retrieveUserDetails(String username) {
        return getUserOrThrow(username);
    }

    @Override
    public PageResponse<UserResponse> retrieveUserList(SearchModal searchModal) {
        int page = Math.max(searchModal.getPage(), 0);

        int size = searchModal.getSize() <= 0
                ? DEFAULT_PAGE_SIZE
                : Math.min(searchModal.getSize(), MAX_PAGE_SIZE);

        String sortBy = resolveSortField(searchModal.getSortBy());

        Sort.Direction direction = resolveSortDirection(
                searchModal.getSortDirection()
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );

        String username = normalizeSearchValue(
                searchModal.getUsername()
        );

        String email = normalizeSearchValue(
                searchModal.getEmail()
        );

        Page<User> userPage =
                userRepo.findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(
                        username,
                        email,
                        pageable
                );

        List<UserResponse> users = userPage.getContent()
                .stream()
                .map(this::toUserResponse)
                .toList();

        return PageResponse.<UserResponse>builder()
                .content(users)
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .numberOfElements(userPage.getNumberOfElements())
                .first(userPage.isFirst())
                .last(userPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        User user = getUserOrThrow(username);
        userRepo.delete(user);
    }

    private User getUserOrThrow(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new BadRequestException(
                                ResponseModal.USER_NOT_EXIST
                        )
                );
    }

    private void validateNewUser(AddUserModal addUserModal) {
        if (userRepo.existsByUsername(addUserModal.getUsername())) {
            throw new BadRequestException(
                    ResponseModal.USERNAME_ALREADY_EXIST
            );
        }

        if (userRepo.existsByEmail(addUserModal.getEmail())) {
            throw new BadRequestException(
                    ResponseModal.EMAIL_ALREADY_EXIST
            );
        }
    }


    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    private String normalizeSearchValue(String value) {
        return value == null ? "" : value.trim();
    }

    private String resolveSortField(String requestedSortField) {
        if (requestedSortField == null ||
                !ALLOWED_SORT_FIELDS.contains(requestedSortField)) {
            return "username";
        }

        return requestedSortField;
    }

    private Sort.Direction resolveSortDirection(String direction) {
        if (direction == null) {
            return Sort.Direction.ASC;
        }

        return "desc".equals(direction.toLowerCase(Locale.ROOT))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
    }
}