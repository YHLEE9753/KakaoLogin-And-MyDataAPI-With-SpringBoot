package com.personal.kakaoLogin.myData.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.personal.kakaoLogin.security.SecurityInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MyDataService {
    private final SecurityInfo securityInfo;

    public String crypto(String regNum) throws Exception {
        AES256 aes256 = new AES256(securityInfo.getAES256Alg(), securityInfo.getAES256Key(), securityInfo.getAES256Iv());
        String cipherText = aes256.encrypt(regNum);
        return cipherText;
    }

    // connect to MyData
    public HashMap<String, Object> connectToMyData(String encryptJumin, String userEmail, String access_Token) {

        String reqURL = "https://datahub-dev.scraping.co.kr/scrap/common/nhis/TreatmentDosageInfoSimple";
        String Token = securityInfo.getMyDataToken();
        HashMap<String, Object> responseInfo = new HashMap<>();

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization",Token);
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setDoOutput(true);

            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            JsonObject jsonBody = new JsonObject();
            jsonBody.addProperty("LOGINOPTION","0");
            jsonBody.addProperty( "JUMIN",encryptJumin);
            jsonBody.addProperty( "DETAILPARSE","3");
            jsonBody.addProperty( "CHILDPARSE","1");
            jsonBody.addProperty( "USERNAME",securityInfo.getTestName());
            jsonBody.addProperty( "HPNUMBER",securityInfo.getTestPhone());
            jsonBody.addProperty( "TELECOMGUBUN","3");

            bw.write(jsonBody.toString());
            bw.flush();

            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);


            JsonObject properties = element.getAsJsonObject().get("data").getAsJsonObject();

            String callbackId = properties.getAsJsonObject().get("callbackId").getAsString();
            String callbackType = properties.getAsJsonObject().get("callbackType").getAsString();
            String callbackData = properties.getAsJsonObject().get("callbackData").getAsString();
            String timeout = properties.getAsJsonObject().get("timeout").getAsString();


            System.out.println(element);
            System.out.println("세부적인 값들 출력");
            System.out.println(callbackId);
            System.out.println(callbackType);
            System.out.println(callbackData);
            System.out.println(timeout);

            responseInfo.put("errCode", "0001");
            responseInfo.put("callbackId", callbackId);
            responseInfo.put("callBackType", callbackType);
            responseInfo.put("callBackData", callbackData);
            responseInfo.put("timeout", timeout);

            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseInfo;
    }


    public void getData(String callbackId, String callBackType) {
        String reqURL = "https://datahub-dev.scraping.co.kr/scrap/captcha";
        String Token = securityInfo.getMyDataToken();

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization",Token);
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setDoOutput(true);

            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            JsonObject jsonBody = new JsonObject();
            jsonBody.addProperty("callbackId",callbackId);
            jsonBody.addProperty( "callbackType",callBackType);

            bw.write(jsonBody.toString());
            bw.flush();

            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
