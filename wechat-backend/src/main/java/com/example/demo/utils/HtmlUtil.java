package com.example.demo.utils;

import com.example.demo.dto.Item;
import com.example.demo.dto.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerliner on 2018/12/27.
 */
public class HtmlUtil {

    public static Result parse(String html) {
        Result result = new Result();
        Document doc = Jsoup.parse(html);
        result.setName(doc.getElementsByClass("fb f16").eachText().get(0));
        result.setZjhm(doc.getElementsByClass("fb f16").eachText().get(1));
        System.out.println(result);
        Elements tables = doc.getElementsByClass("table_box_td f12 p5");
        List<String> writeStr = tables.first().getElementsByClass("td_left_f").eachText();
        List<String> interViewStr = tables.last().getElementsByClass("td_left_f").eachText();
        List<Item> writes = new ArrayList<>();
        List<Item> interViews = new ArrayList<>();
        writeStr.forEach(write -> {
            Item item = new Item();
            String[] td = write.split(" ");
            item.setSubject(td[0]);
            item.setSource(td[1]);
            item.setPass(td[2]);
            item.setNumber(td[3]);
            item.setBatch(td[4]);
            item.setProvince(td[6]);
            writes.add(item);
        });
        result.setWrites(writes);
        interViewStr.forEach(interView -> {
            Item item = new Item();
            String[] td = interView.split(" ");
            item.setSubject(td[0]);
            item.setPass(td[1]);
            item.setNumber(td[2]);
            item.setBatch(td[3]);
            item.setProvince(td[4]);
            interViews.add(item);
        });
        result.setInterViews(interViews);
        System.out.println(result);
        return result;
    }

}
