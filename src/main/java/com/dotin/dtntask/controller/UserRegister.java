package com.dotin.dtntask.controller;

import com.dotin.dtntask.domain.User;
import com.dotin.dtntask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserRegister {

    private User user;

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUser(User user) {
        this.user = user;
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user",user);
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute @Valid User user,
                                 BindingResult bindingResult,
                                 Model model,
                                 HttpSession httpSession){
        if (bindingResult.hasErrors()){
            return "register";
        }
        if (userRepository.findById(user.getEmail()).isPresent()){
            model.addAttribute("emailStatus","duplicate");
            return "register";
        }
        httpSession.setAttribute("userEmail",user.getEmail());
        httpSession.setMaxInactiveInterval(24 * 60 * 60);
        userRepository.save(user);
        return "redirect:/index";
    }
}
