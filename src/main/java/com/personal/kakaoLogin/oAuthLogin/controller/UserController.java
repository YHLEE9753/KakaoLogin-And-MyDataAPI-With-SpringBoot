package com.personal.kakaoLogin.oAuthLogin.controller;

import com.personal.kakaoLogin.oAuthLogin.service.KakaoAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final KakaoAPIService kakao;


    @RequestMapping("/")
    public String index(HttpSession session){

        return "index";
    }

    @RequestMapping("/login")
    public String login(@RequestParam("code") String code, HttpSession session){
        String access_Token = kakao.getAccessToken(code);
        HashMap<String, Object> userInfo = kakao.getUserInfo(access_Token);

        // 클라이언트의 이메일이 존재할 때 세션에 해당 이메일과 토큰 등록
        if(userInfo.get("email") != null){
            session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("access_Token", access_Token);
        }

        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        kakao.kakaoLogout((String)session.getAttribute("access_Token"));
        log.info("session - userid : {}", session.getAttribute("userId"));
        session.removeAttribute("access_Token");
        session.removeAttribute("userId");
        log.info("session - userid : {}", session.getAttribute("userId"));
        return "redirect:/";
    }
}
