package com.postgresql.demo.service;


import com.postgresql.demo.entity.User;
import com.postgresql.demo.modal.*;

import java.util.List;

public interface UserService {

    void saveUser(AddUserModal addUserModal);

    User updateUser(UpdateUserModal updateUserModal);

    User retrieveUserDetails(String username);

    PageResponse<UserResponse> retrieveUserList(SearchModal searchModal);

    void deleteUser(String username);

}
