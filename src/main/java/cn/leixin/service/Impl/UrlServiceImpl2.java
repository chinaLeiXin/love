package cn.leixin.service.Impl;

import cn.leixin.dao.Url;
import cn.leixin.dao.UrlDao;
import cn.leixin.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlServiceImpl2 {
    @Autowired
    UrlDao urlDao;

    public void getURLInfo(String urlInfo, String charset) throws IOException {
        URL url = new URL(urlInfo);
        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
        if (httpUrl.getResponseCode() == 200) {
            InputStream is = httpUrl.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            Pattern p = Pattern.compile("<a\\b[^>]+\\bhref=\"([^\"]*)\"[^>]*>([\\s\\S]*?)</a>");
            String[] split = sb.toString().split("<!--列表开始-->");
            String[] split1 = split[1].toString().split("<!--列表结束-->");
            String[] split2 = split1[0].toString().split("<tr bgcolor=\"#F5FAFE\">");
            for (String s : split2) {
                Matcher matcher = p.matcher(s);
                if (matcher.find(0)) {
                    String urlname = "http://www.mimizyz.com";
                    Url urlmodel = new Url();
                    urlmodel.setUrl(matcher.group(1));
                    urlmodel.setName(matcher.group(2));
                    String avi = urlname + matcher.group(1);
                    String imgname = getAvi(avi, "utf-8");
                    if ("网络链接失败".equals(imgname)&&imgname!=null) {
                        System.out.println("存储失败: " + urlmodel);
                        continue;
                    }
                    urlmodel.setUrlavi(imgname.split("~@~")[0]);
                    urlmodel.setUrlimg(imgname.split("~@~")[1]);
                    urlDao.insert(urlmodel);
                    System.out.println("存储成功: " + urlmodel);
                }
            }
            is.close();
            br.close();
        }
    }

    public List<Url> selectname(Url url) {
        return urlDao.selectname(url);
    }

    public List<Url> selectall() {
        return urlDao.selectall();
    }

    public String getAvi(String avi, String charset) throws IOException {
        URL url = new URL(avi);
        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
        if (httpUrl.getResponseCode() == 200) {
            InputStream is = httpUrl.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String aviname = "没有视频";
            String aviimg = "没有图片";
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            //获取avi播放地址
            Pattern p = Pattern.compile("<a\\b[^>]+\\bhref=\"([^\"]*)\"[^>]*>([\\s\\S]*?)</a>");
            String[] split = sb.toString().split("<ul>");
            String[] split1 = split[2].toString().split("</ul>");
            Matcher matcher = p.matcher(split1[0]);
            if (matcher.find()) {
                aviname = matcher.group(2);
            }
            //获取avi图片地址
            Pattern img = Pattern.compile("<img[^>]+src=\"([^\"]+)\"");
            String[] split2 = sb.toString().split("<div class=\"intro\">");
            String[] split3 = split2[1].toString().split("</div>");
            Matcher matcherimg = img.matcher(split3[0]);
            if (matcherimg.find()) {
                aviimg = matcherimg.group(1);
            }else {
                String[] split4 = sb.toString().split("影片名称");
                String[] split5 = split4[0].toString().split("<tbody>");
                Matcher matcherimg2 = img.matcher(split5[2]);
                if (matcherimg2.find()) {
                    aviimg = matcherimg2.group(1);
                }
            }
            is.close();
            br.close();
            return aviname+"~@~"+aviimg;
        }
        return "网络链接失败";
    }
}
