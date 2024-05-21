package com.example.blissful.blissful.controllers;

import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.blissful.blissful.models.user;
import com.example.blissful.blissful.repository.userrepo;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class usercontroller {
    @Autowired
    private userrepo userRepository;

    @Autowired
    public usercontroller(userrepo userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public ModelAndView getUsers(HttpSession session) {
        ModelAndView mav = new ModelAndView("list-users.html");
        String userType = (String) session.getAttribute("type");

        if (userType == null || userType.equals("user")) {
            return new ModelAndView("redirect:/user/login");
        }

        List<user> userlist = this.userRepository.findAll();
        mav.addObject("userslist", userlist);
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView searchUsers(@RequestParam("username") String username,
            HttpSession session) {
        String userType = (String) session.getAttribute("type");
        if (userType == null || userType.equals("user")) {
            return new ModelAndView("redirect:/user/login");
        }

        // Continue with the search logic if the user is an admin
        ModelAndView mav = new ModelAndView("list-users.html");
        List<user> userList = userRepository.findByUsernameContaining(username);
        mav.addObject("userslist", userList);
        mav.addObject("searchedUsername", username);
        return mav;
    }

    @GetMapping("/registeration")
    public ModelAndView addusers() {
        ModelAndView mav = new ModelAndView("registeration.html");
        user newuser = new user();
        mav.addObject("user", newuser);
        return mav;
    }

    @PostMapping("/registeration")
    public ModelAndView saveUser(@ModelAttribute @Validated user user, BindingResult bindingResult,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {
        ModelAndView mav = new ModelAndView("registeration.html");

        // List to store error messages
        List<String> errorMessages = new ArrayList<>();

        // Check if the email already exists
        user existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            errorMessages.add("Email already exists. Please choose a different email.");
        }
        if (!user.getUsername().matches("[a-zA-Z]+")) {
            errorMessages.add("Username must contain only letters.");
        }
        // Validate password length
        if (user.getPassword().length() < 8) {
            errorMessages.add("Password must be at least 8 characters long.");
        }

        // Confirm password validation
        if (!user.getPassword().equals(confirmPassword)) {
            errorMessages.add("Passwords do not match.");
        }

        // Add error messages to model if any
        if (!errorMessages.isEmpty()) {
            mav.addObject("errorMessages", errorMessages);
            mav.addObject("user", user); // Add user object to retain form data
            return mav;
        }

        // Encode password before saving
        String encodedPass = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(encodedPass);
        user.setType("user");

        // Save the user
        userRepository.save(user);

        // Redirect to a success page
        mav.setViewName("redirect:/user/login"); // Assuming login page is at "/login"
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute("user") @Validated user user, BindingResult bindingResult,
            HttpSession session) {
        ModelAndView mav = new ModelAndView("login.html");

        if (bindingResult.hasErrors()) {
            // Handle validation errors
            mav.addObject("errorMessage", "Invalid email or password");
            return mav;
        }

        user myUser = this.userRepository.findByEmail(user.getEmail());
        if (myUser != null) {
            boolean match = BCrypt.checkpw(user.getPassword(), myUser.getPassword());
            if (match) {
                session.setAttribute("username", myUser.getUsername());
                String userType = myUser.getType();
                session.setAttribute("type", userType);
                if (userType.equals("admin")) {
                    return new ModelAndView("redirect:/user/admindashboard");
                } else {
                    return new ModelAndView("redirect:/home");
                }
            }
        }

        // Handle login failure
        mav.addObject("errorMessage", "Invalid email or password");
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView loginForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("login.html");
        String username = (String) session.getAttribute("username");
        mav.addObject("user", new user());
        mav.addObject("username", username);
        return mav;
    }

    @GetMapping("/admindashboard")
    public ModelAndView getAdminDashboard(HttpSession session) {
        ModelAndView mav = new ModelAndView("admindashboard.html");
        String userType = (String) session.getAttribute("type");
        if (userType == null || userType.equals("user")) {
            return new ModelAndView("redirect:/user/login");
        }
        return mav;
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        // Invalidate the session to logout the user
        session.invalidate();

        // Redirect the user to the login page after logout
        return new ModelAndView("redirect:/user/login");
    }

    @GetMapping("/editpass")
    public ModelAndView editpass() {
        ModelAndView mav = new ModelAndView("editpass.html");
        return mav;
    }

    @PostMapping("/editpass")
    public ModelAndView editPassword(@RequestParam("email") String email,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword,
            HttpSession session) {
        ModelAndView mav = new ModelAndView("editpass.html");

        user currentUser = userRepository.findByEmail(email);
        List<String> errorMessages = new ArrayList<>();
        if (currentUser == null) {
            // Handle case where user is not found in the database
            mav.addObject("errorMessage", "User not found");
            return mav;
        }

        if (!BCrypt.checkpw(oldPassword, currentUser.getPassword())) {
            // Old password doesn't match the user's current password
            mav.addObject("errorMessage", "Old password is incorrect");
            return mav;
        }

        if (newPassword.length() < 8) {
            // New password must be at least 8 characters long
            errorMessages.add("Password must be at least 8 characters long.");
        }

        // Confirm password validation
        if (!newPassword.equals(confirmNewPassword)) {
            // New password and confirm new password don't match
            errorMessages.add("Passwords do not match.");
        }

        // Add error messages to model if any
        if (!errorMessages.isEmpty()) {
            mav.addObject("errorMessages", errorMessages);
            return mav;
        }

        // Encode the new password before saving it
        String encodedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        currentUser.setPassword(encodedNewPassword);

        // Save the updated user object
        userRepository.save(currentUser);

        // Redirect to a success page or show a success message
        mav.setViewName("redirect:/user/login");
        return mav;
    }

    @GetMapping("/delete")
    public ModelAndView deleteuser() {
        ModelAndView mav = new ModelAndView("delete.html");

        // Create an empty User object and add it to the model
        mav.addObject("user", new user());

        return mav;
    }

    @PostMapping("/delete")
    public ModelAndView deleteUser(@ModelAttribute("user") user user) {
        ModelAndView mav = new ModelAndView("redirect:/user");

        user currentUser = userRepository.findByEmail(user.getEmail());

        if (currentUser != null) {
            userRepository.delete(currentUser);
            // Optionally, you can add a success message or perform additional actions
        } else {
            // Handle case where user is not found
            mav.addObject("errorMessage", "User not found");
        }

        return mav;
    }
}