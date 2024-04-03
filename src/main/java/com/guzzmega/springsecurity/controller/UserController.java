package com.guzzmega.springsecurity.controller;

import com.guzzmega.springsecurity.domain.Role;
import com.guzzmega.springsecurity.domain.User;
import com.guzzmega.springsecurity.domain.dto.NewUser;
import com.guzzmega.springsecurity.repository.RoleRepository;
import com.guzzmega.springsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createUser(@RequestBody NewUser newUser){

        var basicRole = roleRepository.findByName(Role.Type.BASIC.name());

        var savedUser = userRepository.findByUsername(newUser.username());
        if(savedUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUsername(newUser.username());
        user.setPassword(passwordEncoder.encode(newUser.password()));
        user.setRoleSet(Set.of(basicRole));

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> findUser(){
        var userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }
}