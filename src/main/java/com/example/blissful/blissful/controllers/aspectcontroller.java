package com.example.blissful.blissful.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.blissful.blissful.models.UserLog;
import com.example.blissful.blissful.repository.UserLogRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class aspectcontroller {
    @Autowired
    private UserLogRepository userlogrep;

    @GetMapping("/aspect")
    public ModelAndView aspect(HttpSession session) {
        ModelAndView mav = new ModelAndView("Aspect.html");
        String userType = (String) session.getAttribute("type");

        // Check if the user is not logged in or is not an admin
        if (userType == null || !userType.equals("admin")) {
            // Redirect to the login page if not logged in or not an admin
            return new ModelAndView("redirect:/user/login");
        }

        // Fetch all user logs from the repository
        List<UserLog> userLogs = userlogrep.findAll();

        // Pass the list of user logs to the template
        mav.addObject("userLogs", userLogs);
        return mav;
    }

}
