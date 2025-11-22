package com.example.quizonline.project.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homeRedirect(Authentication auth) {
        if (auth == null) {
            return "home"; // Public home page (login/register links)
        }

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
            return "redirect:/participant/dashboard";
        }

        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
     @GetMapping("/index1")
    public String homePage() {
        return "home"; // this loads templates/home.html
    }
      @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/participant/dashboard")
    public String participantDashboard() {
        return "participant-dashboard";
    }
    
}
