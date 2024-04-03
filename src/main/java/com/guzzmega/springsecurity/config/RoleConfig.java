package com.guzzmega.springsecurity.config;

import com.guzzmega.springsecurity.domain.Role;
import com.guzzmega.springsecurity.domain.User;
import com.guzzmega.springsecurity.repository.RoleRepository;
import com.guzzmega.springsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class RoleConfig implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public RoleConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        var roleAdmin = roleRepository.findByName(Role.Type.ADMIN.name());
        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                user -> System.out.println("Admin already exists..."),
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("senha123"));
                    user.setRoleSet(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }
}
