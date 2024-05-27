package com.example.blissful.blissful;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
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
import java.util.Calendar;
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
        newUser.setUsername("testuser"); // Ensure username is set
        newUser.setPassword("123"); // Password length less than 8 characters
        String confirmPassword = "123";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -10); // Ensure the date is more than 8 years ago
        newUser.setDob(cal.getTime());
        // Call the method to be tested
        ModelAndView mav = userController.saveUser(newUser, bindingResult, confirmPassword, null);
    
        // Assert the result
        assertEquals("registeration.html", mav.getViewName()); // Should return to registration page
        assertTrue(mav.getModel().containsKey("errorMessages")); // Expecting error messages
        List<String> errorMessages = (List<String>) mav.getModel().get("errorMessages");
        assertEquals(1, errorMessages.size());
        assertEquals("Password must be at least 8 characters long.", errorMessages.get(0));
    }
    @Test
public void testSaveUser_WithMismatchedPasswords_ReturnsError() {
    // Prepare test data
    user newUser = new user();
    newUser.setEmail("test@example.com");
    newUser.setUsername("testuser");
    newUser.setPassword("12345679");
    String confirmPassword = "12345678"; // Confirm password doesn't match
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -10); // Ensure the date is more than 8 years ago
    newUser.setDob(cal.getTime());
    // Call the method to be tested
    ModelAndView mav = userController.saveUser(newUser, bindingResult, confirmPassword, null);

    // Assert the result
    assertEquals("registeration.html", mav.getViewName()); // Should return to registration page
    assertTrue(mav.getModel().containsKey("errorMessages")); // Expecting error messages
    List<String> errorMessages = (List<String>) mav.getModel().get("errorMessages");
    assertEquals(1, errorMessages.size()); // Expecting one error message
    assertEquals("Passwords do not match.", errorMessages.get(0)); // Verify the specific error message
}
@Test
public void testSaveUser_Withwrondob() {
    // Prepare test data
    user newUser = new user();
    newUser.setEmail("test@example.com");
    newUser.setUsername("testuser");
    newUser.setPassword("12345678");
    String confirmPassword = "12345678"; // Confirm password doesn't match
    // Set DOB to a date less than 8 years ago
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -5); // Set the date to 5 years ago
    newUser.setDob(cal.getTime());
    // Call the method to be tested
    ModelAndView mav = userController.saveUser(newUser, bindingResult, confirmPassword, null);

    // Assert the result
    assertEquals("registeration.html", mav.getViewName()); // Should return to registration page
    assertTrue(mav.getModel().containsKey("errorMessages")); // Expecting error messages
    List<String> errorMessages = (List<String>) mav.getModel().get("errorMessages");
    assertEquals(1, errorMessages.size()); // Expecting one error message
    assertEquals("You must be at least 8 years old to register.", errorMessages.get(0)); // Verify the specific error message
}


@Test
public void testSaveUser_WithExistingEmail_ReturnsError() {
    // Prepare test data
    user existingUser = new user();
    existingUser.setEmail("shahd@gmail.com");
    existingUser.setPassword("password");
    existingUser.setUsername("testuser");
    // Ensure username is set
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -10); // Ensure the date is more than 8 years ago
    existingUser.setDob(cal.getTime());
    String confirmPassword = "password";
    
    // Mock repository behavior
    when(userRepository.findByEmail("shahd@gmail.com")).thenReturn(existingUser);

    // Call the method to be tested
    ModelAndView mav = userController.saveUser(existingUser, bindingResult, confirmPassword, null);

    // Assert the result
   // assertEquals("registeration.html", mav.getViewName()); // Should return to registration page
    assertTrue(mav.getModel().containsKey("errorMessages")); // Expecting error messages
    List<String> errorMessages = (List<String>) mav.getModel().get("errorMessages");
    assertEquals(1, errorMessages.size()); // Expecting one error message
    assertEquals("Email already exists. Please choose a different email.", errorMessages.get(0)); // Verify the specific error message
}




//login
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
    @Test
public void testGetUsers_WithAdminSession_ReturnsUserListPage() {
    // Prepare test data
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("type", "admin"); // Simulate admin session

    // Mock userRepository.findAll behavior
    List<user> mockUsers = new ArrayList<>();
    when(userRepository.findAll()).thenReturn(mockUsers);

    // Call the method to be tested
    ModelAndView mav = userController.getUsers(session);

    // Assert the result
    assertEquals("list-users.html", mav.getViewName());
    assertEquals(mockUsers, mav.getModel().get("userslist"));
}
//editpass
@Test
public void testEditPassword_WithValidData_UpdatesPassword() {
    // Prepare test data
    user currentUser = new user();
    currentUser.setEmail("shahdibrahiim@icloud.com");
    currentUser.setPassword("$2a$12$Ekzj8ry4DaaTzGvyyuCBSOkmNIg8xaqey/Pe7pmsG5KSyI5taUYJO"); // Existing hashed password

    // Mock userRepository.findByEmail behavior
    when(userRepository.findByEmail("shahdibrahiim@icloud.com")).thenReturn(currentUser);

    // Prepare HTTP session
    MockHttpSession session = new MockHttpSession();

    // Call the method to be tested
    ModelAndView mav = userController.editPassword("shahdibrahiim@icloud.com", "12345678", "newpassword", "newpassword", session);

    // Assert the result
    assertEquals("redirect:/user/login", mav.getViewName());
}

////////////////
//logout
@Test
public void testLogout_WithValidSession_InvalidatesSession() {
    // Prepare test data
    MockHttpSession session = new MockHttpSession();

    // Call the method to be tested
    ModelAndView mav = userController.logout(session);

    // Assert the result
    assertEquals("redirect:/user/login", mav.getViewName());

    // Attempting to access session attributes after invalidate will throw IllegalStateException
    try {
        session.getAttribute("username");
        fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
        // Expected exception, do nothing
    }
}
//getallusers
@Test
public void testGetUsers() {
    // Prepare test data
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("type", "admin"); // Simulate admin session

    // Mock userRepository.findAll behavior
    List<user> mockUsers = new ArrayList<>();
    when(userRepository.findAll()).thenReturn(mockUsers);

    // Call the method to be tested
    ModelAndView mav = userController.getUsers(session);

    // Assert the result
    assertEquals("list-users.html", mav.getViewName());
    assertEquals(mockUsers, mav.getModel().get("userslist"));
}
//getadmin dashboard
@Test
public void testGetAdminDashboard_WithAdminSession_ReturnsDashboardPage() {
    // Prepare test data
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("type", "admin"); // Simulate admin session

    // Call the method to be tested
    ModelAndView mav = userController.getAdminDashboard(session);

    // Assert the result
    assertEquals("admindashboard.html", mav.getViewName());
}
//searchusers
@Test
public void testSearchUsers_WithAdminSession_ReturnsSearchResults() {
    // Prepare test data
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("type", "admin"); // Simulate admin session

    String searchUsername = "testuser";

    // Mock userRepository.findByUsernameContaining behavior
    List<user> mockUsers = new ArrayList<>();
    when(userRepository.findByUsernameContaining(searchUsername)).thenReturn(mockUsers);

    // Call the method to be tested
    ModelAndView mav = userController.searchUsers(searchUsername, session);

    // Assert the result
   assertEquals("list-users.html", mav.getViewName());
    assertEquals(mockUsers, mav.getModel().get("userslist"));
    assertEquals(searchUsername, mav.getModel().get("searchedUsername"));
}
//delete
@Test
    public void testDeleteUserWithIncorrectData() {
        // Prepare test data
        user userToDelete = new user();
        userToDelete.setEmail("shahdibrahiim@icloud.com");
        String password = "wrongpassword";

        // Mock the behavior of userRepository.findByEmail
        when(userRepository.findByEmail("shahdibrahiim@icloud.com")).thenReturn(null);

        // Call the method to be tested
        ModelAndView mav = userController.deleteUser(userToDelete, password);

        // Verify behavior
        assertEquals(1, mav.getModel().size());
        assertTrue(mav.getModel().containsKey("errorMessage")); // Expecting error message
        assertEquals("User not found", mav.getModel().get("errorMessage"));
    }

    @Test
    public void testDeleteUserWithIncorrectPassword() {
        // Prepare test data
        user existingUser = new user();
        existingUser.setEmail("shahd@example.com");
        existingUser.setPassword("$2a$12$0aW2KBC5z89vT1tOZLV99.CALdJK/MvS91RXQ/Sy5ten2Cug9IRBq"); // Correct hashed password

        // Mock the behavior of userRepository.findByEmail
        when(userRepository.findByEmail("shahd@example.com")).thenReturn(existingUser);

        // Call the method to be tested with incorrect password
        ModelAndView mav = userController.deleteUser(existingUser, "wrongpassword");

        // Verify behavior
        assertEquals(1, mav.getModel().size());
        assertTrue(mav.getModel().containsKey("errorMessage")); // Expecting error message
        assertEquals("Invalid password", mav.getModel().get("errorMessage"));
       
       
    }

    @Test
    public void testDeleteUserWithCorrectData() {
        // Prepare test data
        user existingUser = new user();
        existingUser.setEmail("shahd@example.com");
        existingUser.setPassword("$2a$12$0aW2KBC5z89vT1tOZLV99.CALdJK/MvS91RXQ/Sy5ten2Cug9IRBq"); // Correct hashed password

        // Mock the behavior of userRepository.findByEmail
        when(userRepository.findByEmail("shahd@example.com")).thenReturn(existingUser);

        // Call the method to be tested with correct password
        ModelAndView mav = userController.deleteUser(existingUser, "1234567890");
        assertEquals(0, mav.getModel().size());

        // Verify that userRepository.delete was called with the correct user
        verify(userRepository, times(1)).delete(existingUser);
    }



}
