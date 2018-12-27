package com.example.demo;

import com.example.demo.dto.Result;
import com.example.demo.utils.HtmlUtil;
import jodd.http.Cookie;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jerliner on 2018/12/16.
 */
@RestController
public class DemoController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/sessionId", method = RequestMethod.GET)
    public String getSessionId() {
        // 1.先访问网站拿到sessionId
        HttpResponse response = HttpRequest.post("https://ntcecf4.neea.edu.cn/").send();
        String sessionId = response.cookies()[0].getValue();
        return sessionId;
    }

    @RequestMapping(value = "/yzm", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] yzm(String sessionId) throws Exception {
        // 2.在cookie请求中带上sessionId,拿到验证码
        Cookie cookie = new Cookie("JSESSIONID", sessionId);
        HttpResponse yzmResponse = HttpRequest.get("https://ntcecf4.neea.edu.cn/getYZM").cookies(cookie)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*").send();
        yzmResponse.header("Content-Type", "image/jpeg;charset=utf-8");
        return yzmResponse.bodyBytes();
    }

    @RequestMapping(value = "/source", method = RequestMethod.POST)
    public Result getSource(@RequestBody Map<String, String> params) throws Exception {
        String name = params.get("name");
        String yzm = params.get("yzm");
        String zjhm = params.get("zjhm");
        String sessionId = params.get("sessionId");
        Cookie cookie = new Cookie("JSESSIONID", sessionId);
        Map<String, Object> fromData = new HashMap<>();
        fromData.put("name", name);
        fromData.put("yzm", yzm);
        fromData.put("zjhm", zjhm);
        HttpResponse response = HttpRequest.post("https://ntcecf4.neea.edu.cn/selectScore.do?method=getMyScore")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .form(fromData).cookies(cookie).send();
        response.header("Content-Type", "text/html;charset=utf-8");
        String html = new String(response.body().getBytes("iso-8859-1"), "UTF8");
        Result result = HtmlUtil.parse(html);
        System.out.println(html);
        return result;
    }

}
