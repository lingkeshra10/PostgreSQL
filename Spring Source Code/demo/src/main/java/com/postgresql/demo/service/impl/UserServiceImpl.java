package com.postgresql.demo.service.impl;

import com.postgresql.demo.entity.User;
import com.postgresql.demo.modal.AddUserModal;
import com.postgresql.demo.repository.UserRepo;
import com.postgresql.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User saveUser(AddUserModal addUserModal) {
        try {
            User user = new User();
            user.setName(addUserModal.getName());
            user.setEmail(addUserModal.getEmail());
            user.setUsername(addUserModal.getUsername());
            user.setEncryptPassword(addUserModal.getPassword().hashCode());

            userRepo.save(user);
            return user;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public User updateUser(User user, String name, String email, boolean needToChangeName, boolean needToChangeEmail) {
        if(needToChangeName || needToChangeEmail){
            //If all good proceed to change and if not don't proceed to change
            if(needToChangeName){
                user.setName(name);;
            }

            if(needToChangeEmail){
                user.setEmail(email);
            }
            userRepo.save(user);
        }
        return user;
    }

    @Override
    public User retrieveUserDetails(String username) {
        return userRepo.retrieveByUsername(username);
    }

    @Override
    public List<User> retrieveUserList() {
        return userRepo.findAll();
    }

    @Override
    public boolean findExistByUsername(String username) {
        return userRepo.findExistByUsername(username);
    }

    @Override
    public boolean findExistByEmail(String email) {
        return userRepo.findExistByEmail(email);
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepo.retrieveByUsername(username);
        userRepo.delete(user);
    }


}
