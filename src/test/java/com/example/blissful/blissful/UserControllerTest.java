package com.example.blissful.blissful;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blissful.blissful.controllers.usercontroller;
import com.example.blissful.blissful.models.user;
import com.example.blissful.blissful.repository.userrepo;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

public class UserControllerTest {
    @Mock
    private userrepo userRepository;

    private usercontroller userController;
    private BindingResult bindingResult;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this); // Initialize mocks
        userController = new usercontroller(userRepository);
        bindingResult = mock(BindingResult.class);
    }

    @Test
    public void testSaveUser_WithShortPassword_ReturnsError() {
        // Prepare test data
        user newUser = new user();
        newUser.setEmail("test@example.com");
        newUser.setPassword("123"); // Password length less than 8 characters
        String confirmPassword = "123";

        // Call the method to be tested
        ModelAndView mav = userController.saveUser(newUser, bindingResult,
                confirmPassword, null);

        // Assert the result
        assertEquals("registeration.html", mav.getViewName()); // Should return toregistration page
        assertTrue(mav.getModel().containsKey("errorMessages")); // Expecting
        // errormessages
        assertEquals(1, ((List<String>) mav.getModel().get("errorMessages")).size());
        // Expecting one error message
    }

    @Test
    public void testSaveUser_WithMismatchedPasswords_ReturnsError() {
        // Prepare test data
        user newUser = new user();
        newUser.setEmail("test@example.com");
        newUser.setPassword("12345679");
        String confirmPassword = "12345678"; // Confirm password doesn'tmatch

        // Call the method to be tested
        ModelAndView mav = userController.saveUser(newUser, bindingResult,
                confirmPassword, null);

        // Assert the result
        assertEquals("registeration.html", mav.getViewName()); // Should return
        // toregistration page
        assertTrue(mav.getModel().containsKey("errorMessages")); // Expecting errormessages
        assertEquals(1, ((List<String>) mav.getModel().get("errorMessages")).size());
        // Expecting one error message

    }

    @Test
    public void testSaveUser_WithExistingEmail_ReturnsError() {
        // Prepare test data
        user existingUser = new user();
        existingUser.setEmail("shahd@gmail.com");
        existingUser.setPassword("password");
        String confirmPassword = "password";
        when(userRepository.findByEmail("shahd@gmail.com")).thenReturn(existingUser);

        // Call the method to be tested
        ModelAndView mav = userController.saveUser(existingUser, bindingResult, confirmPassword, null);

        // Assert the result
        assertEquals("registeration.html", mav.getViewName()); // Should return to registration page
        assertTrue(mav.getModel().containsKey("errorMessages")); // Expecting error messages
        assertEquals(1, ((List<String>) mav.getModel().get("errorMessages")).size()); // Expecting one error message
    }

    @Test
    public void testLogin_WithValidCredentials_RedirectsToHome() {
        // Prepare test data
        user existingUser = new user();
        existingUser.setEmail("shahd@gmail.com");
        existingUser.setPassword("$2a$12$0aW2KBC5z89vT1tOZLV99.CALdJK/MvS91RXQ/Sy5ten2Cug9IRBq"); // Correct hashed
                                                                                                  // password

        existingUser.setUsername("testuser");
        existingUser.setType("user");

        // Mock the behavior of userRepository.findByEmail
        when(userRepository.findByEmail("shahd@gmail.com")).thenReturn(existingUser);

        // Prepare user object for login
        user loginUser = new user();
        loginUser.setEmail("shahd@gmail.com");
        loginUser.setPassword("1234567890"); // Correct hashed password

        // Call the method to be tested
        ModelAndView mav = userController.login(loginUser, bindingResult, new MockHttpSession());
        // assertTrue(mav.getModel().containsKey("errorMessages")); // Expecting
        // errormessages
        // assertEquals("Invalid email or password",
        // (mav.getModel().get("errorMessage")));

        // Assert the result
        assertEquals("redirect:/home", mav.getViewName()); // Should redirecttohome
        // page
    }

    @Test
    public void testLogin_WithInValidCredentials_RedirectsToLogin() {
        // Prepare test data
        user existingUser = new user();
        existingUser.setEmail("shhd@gmail.com");
        existingUser.setPassword("$2a$12$0aW2KBC5z89vT1tOZLV99.CALdJK/MvS91RXQ/Sy5ten2Cug9IRBq"); // Correct hashed
                                                                                                  // password

        existingUser.setUsername("testuser");
        existingUser.setType("user");

        // Mock the behavior of userRepository.findByEmail
        when(userRepository.findByEmail("shhd@gmail.com")).thenReturn(existingUser);

        // Prepare user object for login
        user loginUser = new user();
        loginUser.setEmail("shahd@gmail.com");
        loginUser.setPassword("1234567890"); // Correct hashed password

        // Call the method to be tested
        ModelAndView mav = userController.login(loginUser, bindingResult, new MockHttpSession());
        // assertTrue(mav.getModel().containsKey("errorMessages")); // Expecting
        // errormessages
        // assertEquals("Invalid email or password",
        // (mav.getModel().get("errorMessage")));

        // Assert the result
        assertEquals("login.html", mav.getViewName()); // Should redirecttohome
        // page
    }
}
