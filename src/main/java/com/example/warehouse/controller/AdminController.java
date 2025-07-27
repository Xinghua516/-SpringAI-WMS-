package com.example.warehouse.controller;

import com.example.warehouse.entity.Admin;
import com.example.warehouse.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);

        if (adminOpt.isPresent() && adminOpt.get().getPassword().equals(password)) {
            // 登录成功
            session.setAttribute("loggedInAdmin", adminOpt.get());
            return "redirect:/";
        } else {
            // 登录失败
            redirectAttributes.addFlashAttribute("errorMessage", "用户名或密码错误");
            return "redirect:/admin/login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "admin/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("name") String name,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        // 检查用户名是否已存在
        if (adminRepository.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "用户名已存在");
            return "redirect:/admin/register";
        }
        
        // 创建新管理员（基本信息）
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setName(name);
        admin.setGender(""); // 设置默认值
        admin.setPosition(""); // 设置默认值
        admin.setCreatedAt(LocalDateTime.now());
        
        // 保存到数据库
        Admin savedAdmin = adminRepository.save(admin);
        
        // 将管理员信息保存到session中，以便在完善信息时使用
        session.setAttribute("registeringAdmin", savedAdmin);
        
        // 重定向到完善信息页面
        return "redirect:/admin/complete-registration";
    }

    @GetMapping("/complete-registration")
    public String completeRegistrationPage(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("registeringAdmin");
        if (admin == null) {
            return "redirect:/admin/register";
        }
        model.addAttribute("admin", admin);
        return "admin/complete-registration";
    }

    @PostMapping("/complete-registration")
    public String completeRegistration(@RequestParam("gender") String gender,
                                       @RequestParam("position") String position,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        Admin admin = (Admin) session.getAttribute("registeringAdmin");
        if (admin == null) {
            return "redirect:/admin/register";
        }

        // 更新管理员信息
        admin.setGender(gender);
        admin.setPosition(position);

        // 保存更新后的信息
        adminRepository.save(admin);

        // 清除session中的临时信息
        session.removeAttribute("registeringAdmin");

        // 设置登录状态
        session.setAttribute("loggedInAdmin", admin);

        redirectAttributes.addFlashAttribute("successMessage", "注册成功，欢迎使用系统！");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
