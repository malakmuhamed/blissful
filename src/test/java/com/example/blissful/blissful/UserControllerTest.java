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
}