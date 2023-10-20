package com.api.project_management.controller;

import com.api.project_management.model.User;
import com.api.project_management.repository.UserRepository;
import com.api.project_management.utility.DataResponse;
import com.api.project_management.utility.ErrorResponse;
import com.api.project_management.utility.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository users;

    @PostMapping
    public ResponseEntity<DataResponse<?>> createUser(@RequestBody User user){
        User userToCreate;
        try {
            userToCreate = this.users.save(user);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("User creation failed. Please check the required fields.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        UserResponse response = new UserResponse();
        response.set(userToCreate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<?>> getUser(@PathVariable int id){
        User userToFind;
        try {
            userToFind = this.users.findById(id).orElseThrow(Exception::new);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("User with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        UserResponse response = new UserResponse();
        response.set(userToFind);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<?>> updateUser(@PathVariable int id, @RequestBody User user){
        User userToUpdate = this.users.findById(id).orElse(null);
        if (userToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("User with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        try {
            userToUpdate.setDisplayName(user.getDisplayName());
            userToUpdate = this.users.save(userToUpdate);
        } catch (DataIntegrityViolationException e) {
            ErrorResponse error = new ErrorResponse();
            error.set("User update failed. Please check the required fields.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        UserResponse response = new UserResponse();
        response.set(userToUpdate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
