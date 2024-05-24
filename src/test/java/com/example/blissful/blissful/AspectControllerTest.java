package com.example.blissful.blissful;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import com.example.blissful.blissful.controllers.aspectcontroller;
import com.example.blissful.blissful.models.UserLog;
import com.example.blissful.blissful.repository.UserLogRepository;

import jakarta.servlet.http.HttpSession;

public class AspectControllerTest {

    @InjectMocks
    private aspectcontroller aspectController;

    @Mock
    private UserLogRepository userLogRepository;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAspectNotLoggedIn() {
        // Mock session behavior
        when(session.getAttribute("type")).thenReturn(null);

        // Call the method to be tested
        ModelAndView mav = aspectController.aspect(session);

        // Verify behavior
        assertEquals("redirect:/user/login", mav.getViewName());
    }

    @Test
    public void testAspectNotAdmin() {
        // Mock session behavior
        when(session.getAttribute("type")).thenReturn("user");

        // Call the method to be tested
        ModelAndView mav = aspectController.aspect(session);

        // Verify behavior
        assertEquals("redirect:/user/login", mav.getViewName());
    }

    @Test
    public void testAspectAdmin() {
        // Mock session behavior
        when(session.getAttribute("type")).thenReturn("admin");

        // Prepare test data
        List<UserLog> userLogs = new ArrayList<>();
        userLogs.add(new UserLog());
        userLogs.add(new UserLog());

        // Mock repository behavior
        when(userLogRepository.findAll()).thenReturn(userLogs);

        // Call the method to be tested
        ModelAndView mav = aspectController.aspect(session);

        // Verify behavior
        assertEquals("Aspect.html", mav.getViewName());
        assertTrue(mav.getModel().containsKey("userLogs"));
        assertEquals(userLogs, mav.getModel().get("userLogs"));
    }

    @Test
public void testAspectAdminEmptyLogs() {
    // Mock session behavior
    when(session.getAttribute("type")).thenReturn("admin");

    // Prepare test data
    List<UserLog> emptyLogs = Collections.emptyList();

    // Mock repository behavior
    when(userLogRepository.findAll()).thenReturn(emptyLogs);

    // Call the method to be tested
    ModelAndView mav = aspectController.aspect(session);

    // Verify behavior
    assertEquals("Aspect.html", mav.getViewName());
    assertTrue(mav.getModel().containsKey("userLogs"));
    assertEquals(emptyLogs, mav.getModel().get("userLogs"));
}
}
