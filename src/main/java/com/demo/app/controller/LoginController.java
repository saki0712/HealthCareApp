package com.demo.app.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.entity.SignupForm;
import com.demo.service.UserDetailsServiceImpl;

@Controller
@RequestMapping("/")
public class LoginController {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping
    public String index() {
        return "app/index";
    }

    @GetMapping("/login")
    public String login() {
        return "app/login";
    }

    @GetMapping("/signup")
    public String newSignup(SignupForm signupForm) {
        return "app/signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated SignupForm signupForm, BindingResult result, Model model,
            HttpServletRequest request) {
        if (result.hasErrors()) {
            return "app/signup";
        }

        if (userDetailsServiceImpl.isExistUser(signupForm.getUsername())) {
            model.addAttribute("signupError", "ユーザー名 " + signupForm.getUsername() + "は既に登録されています");
            return "app/signup";
        }

        try {
            userDetailsServiceImpl.register(signupForm.getUsername(), signupForm.getPassword(), "ROLE_USER");
        } catch (DataAccessException e) {
            model.addAttribute("signupError", "ユーザー登録に失敗しました");
            return "app/signup";
        }

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken == false) {
            SecurityContextHolder.clearContext();
        }

        try {
            request.login(signupForm.getUsername(), signupForm.getPassword());
        } catch (ServletException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }
}