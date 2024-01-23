package com.example.demo.user;

import com.example.demo.mail.MailSenderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailSenderService mailSenderService;

    public UserService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       MailSenderService mailSenderService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mailSenderService = mailSenderService;
    }

    public boolean registerUser(String username, String rawPassword) {
        User userToAdd = new User();
        List<User> allUsers = userRepository.findAll();
        if (username.isEmpty() || rawPassword.isEmpty()) {
            return false;
        }
        for (User user : allUsers) {
            if (user.getEmail().equalsIgnoreCase(username))
                return false;
        }
        userToAdd.setEmail(username);
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        userToAdd.setPassword(encryptedPassword);
        List<UserRole> list = Collections.singletonList(new UserRole(userToAdd, Role.ROLE_USER));
        userToAdd.setRoles(new HashSet<>(list));
        userRepository.save(userToAdd);
        return true;
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findAllWithoutCurrentUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getEmail().equals(currentUser.getName()))
                .collect(Collectors.toList());
    }

    public User findCurrentUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getEmail().equals(currentUser.getName()))
                .collect(Collectors.toList())
                .get(0);
    }


    public String findRolesOfCurrentUser() {
        User user = findCurrentUser();
        Set<UserRole> userRoles = user.getRoles();
        return userRoles.toString();
    }

    public void sendPasswordResetLink(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String key = UUID.randomUUID().toString();
            user.setPasswordResetKey(key);
            userRepository.save(user);
            try {
                mailSenderService.sendPasswordResetLink(email, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void updateUserPassword(String key, String password) {
        userRepository.findByPasswordResetKey(key).ifPresent(
                user -> {
                    user.setPassword(passwordEncoder.encode(password));
                    user.setPasswordResetKey(null);
                    userRepository.save(user);
                }
        );
    }

    public void revokeRole(Long userId, UserRole roleToRevoke) {
        User user = findUserByIdFromRepository(userId);
        Set<UserRole> userRoles = user.getRoles();
        userRoles.remove(roleToRevoke);
        userRepository.save(user);
    }

    public void assignRole(Long userId, UserRole roleToAssign) {
        User user = findUserByIdFromRepository(userId);
        Set<UserRole> userRoles = user.getRoles();
        userRoles.add(roleToAssign);
        userRepository.save(user);
    }

    @Transactional
    public void editUserProfile(User updatedUser, String newPassword) {
        User currentUser = findCurrentUser();
        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        String encryptedPassword = passwordEncoder.encode(newPassword);
        currentUser.setPassword(encryptedPassword);
        userRepository.save(currentUser);
    }

    private User findUserByIdFromRepository(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

}