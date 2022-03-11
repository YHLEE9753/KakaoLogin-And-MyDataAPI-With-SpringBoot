package com.personal.kakaoLogin.myData.controller;

import com.personal.kakaoLogin.myData.service.MyDataService;
import com.personal.kakaoLogin.oAuthLogin.service.KakaoAPIService;
import com.personal.kakaoLogin.security.SecurityInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@Slf4j
public class MyDataController {
    @Autowired
    private MyDataService dataService;
    @Autowired
    private SecurityInfo securityInfo;

    @RequestMapping("/mydata")
    public String home(){
        log.info("myData");
        return "myData/mydataHome";
    }

    @RequestMapping("/connection")
    public String connect(HttpSession session) throws Exception {
        String registrationNumber = securityInfo.getTestJumin();
        String encode = dataService.crypto(registrationNumber);
        System.out.println(encode);

        HashMap<String, Object> responseInfo = dataService.connectToMyData(encode, String.valueOf(session.getAttribute("UserId")), String.valueOf(session.getAttribute("access_Token")));
        session.setAttribute("errCode", String.valueOf(responseInfo.get("errCode")));
        session.setAttribute("callbackId", String.valueOf(responseInfo.get("callbackId")));
        session.setAttribute("callBackType", String.valueOf(responseInfo.get("callBackType")));
        session.setAttribute("callBackData", String.valueOf(responseInfo.get("callBackData")));
        session.setAttribute("timeout", String.valueOf(responseInfo.get("timeout")));


        log.info("Connect");
        return "myData/connectionKakao";
    }

    @RequestMapping("/connection/data")
    public String connectionData(HttpSession session){
        String callbackId = (String) session.getAttribute("callbackId");
        String callBackType = (String) session.getAttribute("callBackType");
        dataService.getData(callbackId,callBackType);
        return "myData/dataSample";
    }

}
