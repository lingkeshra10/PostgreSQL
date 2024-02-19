package com.postgresql.demo.service;


import com.postgresql.demo.entity.User;
import com.postgresql.demo.modal.AddUserModal;

import java.util.List;

public interface UserService {

    User saveUser(AddUserModal addUserModal);

    User updateUser(User user, String name, String email, boolean needToChangeName, boolean needToChangeEmail);

    User retrieveUserDetails(String username);

    List<User> retrieveUserList();

    boolean findExistByUsername(String username);

    boolean findExistByEmail(String email);

    void deleteUser(String username);

}
