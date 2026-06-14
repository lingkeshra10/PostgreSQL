package com.postgresql.demo.controller;

import com.postgresql.demo.entity.User;
import com.postgresql.demo.modal.*;
import com.postgresql.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/psql/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/status/check")
    public String status() {
        return "Its working";
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public ResponseEntity<Void> addUser(@RequestBody AddUserModal addUserModal) {
        userService.saveUser(addUserModal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/update", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<ResponseModal> updateUser(@RequestBody UpdateUserModal updateUserModal) {
        User user = userService.updateUser(updateUserModal);

        ResponseModal responseModal = new ResponseModal();
        responseModal.setCode(ResponseModal.UPDATE_USER_SUCCESS);
        responseModal.setMessage(ResponseModal.getResponseMsg(ResponseModal.UPDATE_USER_SUCCESS));
        responseModal.setData(user.toString());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseModal);
    }

    @RequestMapping(value = "/isUserExist/{username}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<ResponseModal> isUserExistByUsername(@PathVariable String username) {
        User user = userService.retrieveUserDetails(username);

        ResponseModal responseModal = new ResponseModal();
        responseModal.setCode(ResponseModal.USER_EXIST);
        responseModal.setMessage(ResponseModal.getResponseMsg(ResponseModal.USER_EXIST));
        responseModal.setData(user.toString());

        return ResponseEntity.ok(responseModal);
    }

    @GetMapping(value = "/listUser", produces = "application/json")
    public ResponseEntity<ResponseModal> retrieveUserList(@ModelAttribute SearchModal searchModal) {
        PageResponse<UserResponse> result = userService.retrieveUserList(searchModal);

        ResponseModal responseModal = ResponseModal.builder()
                .code(ResponseModal.RETRIEVE_USER_LIST_SUCCESS)
                .message(ResponseModal.getResponseMsg(ResponseModal.RETRIEVE_USER_LIST_SUCCESS))
                .data(result)
                .build();

        return ResponseEntity.ok(responseModal);
    }

    @RequestMapping(value = "/delete/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}
