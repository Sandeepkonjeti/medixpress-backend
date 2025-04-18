package com.medixpress.user_service.controller;

import com.medixpress.user_service.dto.LoginRequest;
import com.medixpress.user_service.dto.LoginResponse;
import com.medixpress.user_service.dto.UserDTO;
import com.medixpress.user_service.exception.UserNotExistException;
import com.medixpress.user_service.entity.User;
import com.medixpress.user_service.util.JwtUtil;
import com.medixpress.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.registerUser(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestHeader String token, @RequestBody UserDTO userDTO) {
        Long id = Long.valueOf(jwtUtil.extractId(token));
        User updated = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updated);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }
    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get")
    public ResponseEntity<User> getUserByIdForUser(@RequestHeader String token) {
        Long userId = Long.valueOf(jwtUtil.extractId(token));
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Delete
    @DeleteMapping
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
//        Long userId = Long.valueOf(jwtUtil.extractId(token));
        Long userId = (Long) request.getAttribute("id");
        System.out.println(userId);
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("Deleted User successfully");
        } catch (Exception e) {
            throw new UserNotExistException("User Not Found!");
        }
    }

    @GetMapping("/customer")
    public ResponseEntity<List<User>> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    @GetMapping("/pharmacy")
    public ResponseEntity<List<User>> getAllPharmacy() {
        return ResponseEntity.ok(userService.getAllPharmacy());
    }
}
