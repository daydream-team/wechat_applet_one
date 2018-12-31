package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.CetResult;
import com.example.demo.dto.CetYzmResult;
import com.example.demo.dto.Result;
import com.example.demo.utils.HtmlUtil;
import jodd.http.Cookie;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.http.ProxyInfo;
import jodd.http.net.SocketHttpConnectionProvider;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;
import sun.net.util.IPAddressUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jerliner on 2018/12/16.
 */
@RestController
public class DemoController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(HttpServletRequest request) {
        Map<String, String> map = getHost(request);
        String ip = map.get("ip");
        System.out.println();
        System.out.println(map.get("port"));
        System.out.println(internalIp(ip));
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
        Result result = new Result();
        try {
            result = HtmlUtil.parse(html);
        } catch (Exception e) {
            result.setCode("ERROR");
            return result;
        }
        System.out.println(html);
        result.setCode("OK");
        return result;
    }

    //-----------------------------CET-6--------------------------//

    @RequestMapping(value = "/cetYzm", method = RequestMethod.GET)
    public CetYzmResult getCETYzm(String IdCard) {
        CetYzmResult result = new CetYzmResult();
        HttpResponse response = HttpRequest.get("http://cet.neea.edu.cn/cet/")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36").send();
        String str = response.header("Date");
        Date date = new Date(str);
        System.out.println(date.getTime() / 1000);
        long time = date.getTime() / 1000;
        result.setTimestamp(time);

        double random = Math.random();
        String imgUrl = "http://cache.neea.edu.cn/Imgs.do?c=CET&ik=" + IdCard + "&t=" + random;
        System.out.println(imgUrl);
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        headers.put("Connection", "keep-alive");
        headers.put("Cache-Control", "no-cache");
        headers.put("Pragma", "no-cache");
        headers.put("Referer", "http://cet.neea.edu.cn/cet/");
        Cookie[] cookie = new Cookie[]{
                new Cookie("Hm_lvt_dc1d69ab90346d48ee02f18510292577", String.valueOf(time)),
                new Cookie("Hm_lpvt_dc1d69ab90346d48ee02f18510292577", String.valueOf(time)),
                new Cookie("language", "1")
        };
        HttpResponse imgResponse = HttpRequest.get(imgUrl).header(headers).cookies(cookie).send();
        result.setKey(imgResponse.header("Set-Cookie").split(";")[0].split("=")[1]);
        System.out.println(imgResponse.header("Set-Cookie"));
        System.out.println(imgResponse.body());
        String body = imgResponse.body();
        String yzmUrl = body.substring(body.indexOf("\"") + 1, body.lastIndexOf("\""));
        result.setImgBase64(encodeImgageToBase64(yzmUrl));
        return result;
    }

    @RequestMapping(value = "/cetSource", method = RequestMethod.POST)
    public CetResult cetSource(@RequestBody Map<String, String> params) throws UnsupportedEncodingException {
        String time = params.get("timestamp");
        String key = params.get("key");
//        String cet = params.get("cet"); // six or four
        String IdCard = params.get("IdCard");
        String name = params.get("name");
        String yzm = params.get("yzm");
        String data;
        if ("2".equals(IdCard.substring(9,10))) {
            data = "CET6_181_DANGCI," + IdCard + "," + name;
        } else {
            data = "CET4_181_DANGCI," + IdCard + "," + name;
        }
        String queryUrl = "http://cache.neea.edu.cn/cet/query";
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        headers.put("Connection", "keep-alive");
        headers.put("Cache-Control", "no-cache");
        headers.put("Pragma", "no-cache");
        headers.put("Referer", "http://cet.neea.edu.cn/cet/");
        Map<String, Object> fromData = new HashMap<>();
        fromData.put("data", data);
        fromData.put("v", yzm);
        Cookie[] cookie = new Cookie[]{
                new Cookie("Hm_lvt_dc1d69ab90346d48ee02f18510292577", time),
                new Cookie("Hm_lpvt_dc1d69ab90346d48ee02f18510292577", time),
                new Cookie("language", "1"),
                new Cookie("BIGipServercache.neea.edu.cn_pool", key)
        };
        HttpResponse response = HttpRequest.post(queryUrl).form(fromData).cookies(cookie).header(headers).send();
        String str = new String(response.body().getBytes("iso-8859-1"), "UTF8");
        System.out.println(str);
        String subStr = str.substring(str.indexOf("\"") + 1, str.lastIndexOf("\""));
        JSONObject json = JSON.parseObject(subStr);
        String code = json.getString("error");
        CetResult result = new CetResult();
        if (!StringUtils.isEmpty(code)) {
            result.setCode("ERROR");
            return result;
        }
        result.setCode("OK");
        Double sum = Double.parseDouble(json.getString("s"));
        Double listen = Double.parseDouble(json.getString("l"));
        Double write = Double.parseDouble(json.getString("w"));
        Double read = Double.parseDouble(json.getString("r"));
        result.setListen(listen);
        result.setReading(read);
        result.setWriteAndTranslate(write);
        result.setSum(sum);
        return result;
    }

    private Map<String, String> getHost(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        String ip = request.getRemoteAddr();
        int port = request.getRemotePort();
        map.put("ip", ip);
        map.put("port", String.valueOf(port));
        return map;
    }

    private String encodeImgageToBase64(String imageUrl) {
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(imageUrl));
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String encrypted = encoder.encode(outputStream.toByteArray()).replace("\r\n", "").replace("\n", "");
        return "data:image/jpeg;base64," + encrypted;
    }

    private boolean internalIp(String ip) {
        try {
            byte[] addr = IPAddressUtil.textToNumericFormatV4(ip);
            final byte b0 = addr[0];
            final byte b1 = addr[1];
            //10.x.x.x/8
            final byte SECTION_1 = 0x0A;
            //172.16.x.x/12
            final byte SECTION_2 = (byte) 0xAC;
            final byte SECTION_3 = (byte) 0x10;
            final byte SECTION_4 = (byte) 0x1F;
            //192.168.x.x/16
            final byte SECTION_5 = (byte) 0xC0;
            final byte SECTION_6 = (byte) 0xA8;
            switch (b0) {
                case SECTION_1:
                    return true;
                case SECTION_2:
                    if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                        return true;
                    }
                case SECTION_5:
                    switch (b1) {
                        case SECTION_6:
                            return true;
                    }
                default:
                    return false;

            }
        } catch (Exception e) {
            return true;
        }
    }

}
