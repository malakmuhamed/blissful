package com.example.blissful.blissful.logging;

import com.example.blissful.blissful.models.UserLog;
import com.example.blissful.blissful.repository.UserLogRepository;

import jakarta.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

// @Aspect
// @Component
// public class UserLogging {

//     @Autowired
//     private UserLogRepository userLogRepository;

//     @Before("execution(* com.example.blissful.blissful.controllers.usercontroller.*(..))")
//     public void beforeUserControllerMethod(JoinPoint joinPoint) {
//         // Extract information from the JoinPoint
//         String methodName = joinPoint.getSignature().getName();
//         String className = joinPoint.getTarget().getClass().getSimpleName();

//         // Log the method name and class name
//         System.out.println("Method Executed: " + methodName + " in class: " + className);

//         // Extract information from the session
//         HttpSession session = ((ServletRequestAttributes) RequestContextHolder
//                 .currentRequestAttributes()).getRequest().getSession();
//         String username = (String) session.getAttribute("username");

//         // Extract information from the request
//         jakarta.servlet.http.HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                 .currentRequestAttributes()).getRequest();
//         String pageVisited = request.getRequestURI();
//         Date loginTime = new Date();

//         // Save user log to the database
//         UserLog userLog = new UserLog();
//         userLog.setUserId(username);
//         userLog.setLoginTime(loginTime);
//         userLog.setPageVisited(pageVisited);
//         userLogRepository.save(userLog);
//     }

// }
@Aspect
@Component
public class UserLogging {

    @Autowired
    private UserLogRepository userLogRepository;

    @Before("execution(* com.example.blissful.blissful.controllers.usercontroller.*(..)) && !execution(* com.example.blissful.blissful.controllers.usercontroller.login(..))")
    public void beforeUserControllerMethod(JoinPoint joinPoint) {
        // Extract information from the JoinPoint
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // Log the method name and class name
        System.out.println("Method Executed: " + methodName + " in class: " + className);

        // Extract information from the session
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest().getSession();
        String username = (String) session.getAttribute("username");

        // Extract information from the request
        jakarta.servlet.http.HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        String pageVisited = request.getRequestURI();
        Date loginTime = new Date();

        // Save user log to the database
        UserLog userLog = new UserLog();
        userLog.setUserId(username);
        userLog.setLoginTime(loginTime);
        userLog.setPageVisited(pageVisited);
        userLogRepository.save(userLog);
    }
}
