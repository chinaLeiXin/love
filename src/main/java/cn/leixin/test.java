package cn.leixin;

import cn.leixin.dao.Url;
import cn.leixin.dao.UrlDao;
import cn.leixin.service.Impl.UrlServiceImpl;
import cn.leixin.utlis.JsoupHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class test {
    @Autowired
    UrlServiceImpl urlService;
    @Autowired
    RedisTemplate template;
    @Test
    public void redis(){
//        template.opsForValue().set("page",1);
        Integer page = (Integer) template.opsForValue().get("page");
        String pagenumber = (String) template.opsForValue().get("pagenumber");
        System.out.println(page+"----"+pagenumber);
    }
    @Test
    public void saveUrl() throws Exception {
        for (int i=1;i<2;i++) {
            if (i==1) {
                String url = "http://www.mimizyz.com/vod-show-p.html";
                urlService.getURLInfo(url,"utf-8",1);
            }
            String url="http://www.mimizyz.com/vod-show-p-"+i+".html";
            urlService.getURLInfo(url,"utf-8",1);
        }
    }
    @Test
    public void selectall(){
        Url url = new Url();
        url.setName("阿萨德");
        List<Url> selectall = urlService.selectall();
        PageInfo p = new PageInfo(selectall);
        for (Url ur:selectall
             ) {
            System.out.println(ur.toString());
        }
    }
    @Test
    public void selectname(){
        String a ="asda";
        System.out.println(StringUtils.isBlank(a));
        System.out.println((Integer) template.opsForValue().get("number"));
    }

    @Test
    public void se() throws Exception {
        String avi ="http://www.sezyz1.com/oumeixingai/28254/";
        Url urlmodel = new Url();
        urlmodel.setUrl(avi);
        urlmodel.setUrlimg(urlService.getimg(avi));
        List<String> listAvi = urlService.getAvi(avi);
        for (String address :listAvi){
            urlmodel.setUrlavi(address);
            System.out.println(urlmodel);
        }
    }
    //单页测试
    @Test
    public void cutFormTset() throws Exception {
        String avi ="http://www.sezyz1.com/vod-show-p-1.html";
        urlService.getURLInfo(avi,"utf-8",1);
    }

    @Test
    public void sdsade() throws Exception {
        URL url = new URL("http://www.sezyz1.com/vod-show-p-1.html");
        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
        if (httpUrl.getResponseCode() == 200) {
            InputStream is = httpUrl.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString().trim());
        }
    }
}