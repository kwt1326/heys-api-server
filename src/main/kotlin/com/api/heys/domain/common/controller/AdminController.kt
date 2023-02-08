package com.api.heys.domain.common.controller

import com.api.heys.constants.enums.Gender
import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.domain.user.service.UserService
import com.api.heys.utils.UserUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("admin")
class AdminController(
    @Autowired private val userUtil: UserUtil,
    @Autowired private val userService: UserService
) {
    @GetMapping("/login")
    fun loginPage(model: Model): String {
        return if (userUtil.isLogging(userService)) "redirect:/" else "login"
    }

    @GetMapping("/sign-up")
    fun signUpPage(model: Model): String {
        model.addAttribute("dto", SignUpData(
            phone = "", username = "", password = "", age = 0, gender = Gender.NonBinary
        ))
        return if (userUtil.isLogging(userService)) "redirect:/" else "signup"
    }

    @PostMapping("/sign-up-result")
    fun signUpResultPage(@ModelAttribute("dto") dto: SignUpData, model: Model): String {
        val token: String? = userService.signUp(dto, "admin")
        if (token != null) {
            model.addAttribute("msg", "회원가입에 성공하였습니다.")
            return "redirect:/login"
        }
        model.addAttribute("msg", "올바르지 않은 요청입니다.")
        model.addAttribute("dto", dto)
        return "redirect:/signup"
    }

    @GetMapping
    fun mainPage(model: Model): String {
        return if (userUtil.isLogging(userService)) "redirect:/" else "login"
    }
}