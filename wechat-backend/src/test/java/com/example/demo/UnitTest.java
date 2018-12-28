//package com.example.demo;
//
//import jodd.http.Cookie;
//import jodd.http.HttpRequest;
//import jodd.http.HttpResponse;
//import org.joda.time.DateTime;
//import org.junit.Test;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by Jerliner on 2018/12/22.
// */
//public class UnitTest {
//
//    @Test
//    public void testJodd() {
//        HttpResponse response = HttpRequest.get("http://cet.neea.edu.cn/cet/")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36").send();
//        String str = response.header("Date");
//        Date date = new Date(str);
//        System.out.println(date.getTime() / 1000);
//        long time = date.getTime() / 1000;
//
//        double random = Math.random();
//        String imgUrl = "http://cache.neea.edu.cn/Imgs.do?c=CET&ik=" + "451010181207023" + "&t=" + random;
//        System.out.println(imgUrl);
//        Map<String, String> headers = new HashMap<>();
//        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
//        headers.put("Connection", "keep-alive");
//        headers.put("Cache-Control", "no-cache");
//        headers.put("Pragma", "no-cache");
//        headers.put("Referer", "http://cet.neea.edu.cn/cet/");
//        Cookie[] cookie = new Cookie[]{
//                new Cookie("Hm_lvt_dc1d69ab90346d48ee02f18510292577", String.valueOf(time)),
//                new Cookie("Hm_lpvt_dc1d69ab90346d48ee02f18510292577", String.valueOf(time)),
//                new Cookie("language", "1")
//        };
//        HttpResponse imgResponse = HttpRequest.get(imgUrl).header(headers).cookies(cookie).send();
//        System.out.println(imgResponse.header("Set-Cookie"));
//        System.out.println(imgResponse.body());
//    }
//
//    @Test
//    public void testCET6() {
//        String queryUrl = "http://cache.neea.edu.cn/cet/query";
//        Map<String, String> headers = new HashMap<>();
//        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
//        headers.put("Connection", "keep-alive");
//        headers.put("Cache-Control", "no-cache");
//        headers.put("Pragma", "no-cache");
//        headers.put("Referer", "http://cet.neea.edu.cn/cet/");
//        Map<String, Object> fromData = new HashMap<>();
//        fromData.put("data", "CET6_181_DANGCI,451010181207023,李燕娟");
//        fromData.put("v", "enba");
//        Cookie[] cookie = new Cookie[]{
//                new Cookie("Hm_lvt_dc1d69ab90346d48ee02f18510292577", String.valueOf(1545828107)),
//                new Cookie("Hm_lpvt_dc1d69ab90346d48ee02f18510292577", String.valueOf(1545828107)),
//                new Cookie("language", "1"),
//                new Cookie("BIGipServercache.neea.edu.cn_pool", "580962314.39455.0000")
//        };
//        HttpResponse response = HttpRequest.post(queryUrl).form(fromData).cookies(cookie).header(headers).send();
//        System.out.println(response.body());
//    }
//
//    @Test
//    public void testYZM() {
//        // 1.先访问网站拿到sessionId
//        HttpResponse response = HttpRequest.post("https://ntcecf4.neea.edu.cn/").send();
//        String sessionId = response.cookies()[0].getValue();
//        // 2.在cookie请求中带上sessionId,拿到验证码
//        Cookie[] cookie = new Cookie[]{
//                new Cookie("JSESSIONID", sessionId),
//                new Cookie("path", "/")
//        };
//        HttpResponse yzmResponse = HttpRequest.get("https://ntcecf4.neea.edu.cn/getYZM").cookies(cookie).send();
//    }
//
//    @Test
//    public void getScore() {
//        String url = "https://ntcecf4.neea.edu.cn/selectScore.do?method=getMyScore";
//        /**
//         * form data
//         * name: 李燕丽
//         * zjhm: 452123199404022545
//         * yzm: pfyt
//         *
//         * POST
//         *
//         * request header
//         * Content-Type: application/x-www-form-urlencoded
//         Cookie: JSESSIONID=F45FA077324DC782A5B56B43888CFD0D
//         Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng....
//         *
//         * response header
//         * Content-Type: image/jpeg;charset=utf-8
//         *
//         */
//    }
//
//    @Test
//    public void testCode() throws UnsupportedEncodingException {
//        String test = "<script>document.domain='neea.edu.cn';</script><script>parent.result.callback(\"{z:'451010181207023',n:'æ\u009D\u008Eç\u0087\u0095å¨\u009F',x:'å¹¿è¥¿å¤§å\u00AD¦',s:296.00,t:0,l:90,r:107,w:99,kyz:'--',kys:'--'}\");</script>";
//        System.out.println(new String(test.getBytes("iso-8859-1"), "UTF8"));
//    }
//
//}
