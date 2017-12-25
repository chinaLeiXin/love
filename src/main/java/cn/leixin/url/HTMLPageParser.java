package cn.leixin.url;

import cn.leixin.dao.Url;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLPageParser {
    public static void main(String[] args) throws Exception {

        String url = "http://www.mimizyz.com/qiangjianluanlun/41979/play-41979-0-1.html";
        getURLInfo(url, "utf-8");
    }

    public static String getURLInfo(String urlInfo, String charset) throws Exception {
        //读取目的网页URL地址，获取网页源码
        URL url = new URL(urlInfo);
        HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
        InputStream is = httpUrl.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        System.out.println(sb);
        is.close();
        br.close();
        return "";
    }
}
