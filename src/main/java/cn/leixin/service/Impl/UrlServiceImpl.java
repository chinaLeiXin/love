package cn.leixin.service.Impl;

import cn.leixin.dao.Url;
import cn.leixin.dao.UrlDao;
import cn.leixin.service.UrlService;
import cn.leixin.utlis.JsoupHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlServiceImpl implements UrlService {
    @Autowired
    UrlDao urlDao;
    @Autowired
    RedisTemplate template;

    public void getURLInfo(String urlInfo, String charset,Integer page) throws Exception {
        Boolean type =false;
        Integer i =0;
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
            for (String s:split2) {
                Matcher matcher = p.matcher(s);
                if (matcher.find(0)) {
                    boolean bl = false;
                    String urlname = "http://www.sezyz1.com";
                    Url urlmodel = new Url();
                    urlmodel.setName(matcher.group(2));
                    String avi = urlname + matcher.group(1);
                    urlmodel.setUrl(avi);
                    urlmodel.setUrlimg(getimg(avi));
                    List<String> listAvi = getAvi(avi);
                    for (String address :listAvi){
                        urlmodel.setUrlavi(address);
                        if (urlDao.insert(urlmodel)>0){
                            bl=true;
                        }
                    }
                    if (bl){
                        template.opsForValue().set("pagenumber",page+"--"+i);
                        template.opsForValue().set("number",i);
                    }
                }
            }
            is.close();
            br.close();
        }
    }
    public List<Url> selectname(Url url) {
        return urlDao.selectname(url);
    }
    //抽取
    public void rule(Pattern p,String s,Integer page,Integer i) throws Exception {
        Matcher matcher = p.matcher(s);
        if (matcher.find(0)) {
            boolean bl = false;
            String urlname = "http://www.sezyz1.com";
            Url urlmodel = new Url();
            urlmodel.setName(matcher.group(2));
            String avi = urlname + matcher.group(1);
            urlmodel.setUrl(avi);
            urlmodel.setUrlimg(getimg(avi));
            List<String> listAvi = getAvi(avi);
            for (String address :listAvi){
                urlmodel.setUrlavi(address);
                if (urlDao.insert(urlmodel)>0){
                    bl=true;
                }
            }
            if (bl){
                template.opsForValue().set("pagenumber",page+"--"+i);
                template.opsForValue().set("number",i);
            }
        }
    }

    public List<Url> selectall() {
        return urlDao.selectall();
    }

    public List<String> getAvi(String avi) throws Exception {
        JsoupHelper jsoupHelper = new JsoupHelper();
        List<String> img = jsoupHelper.fecthAttr(avi,"/html/body/div[6]/table[2]/tbody/tr[3]/td/ul/li/input", "value");
        return img;
    }
    public String getimg(String avi) throws Exception {
        JsoupHelper jsoupHelper = new JsoupHelper();
        List<String> img = jsoupHelper.fecthAttr(avi,"/html/body/table[2]/tbody/tr/td/div/img", "src");
        if (img.size()>0) {
            return img.get(0);
        }
        img = jsoupHelper.fecthAttr(avi,"/html/body/table[2]/tbody/tr/td/img", "src");
        if (img.size()>0){
            return img.get(0);
        }
        return "没有图片存在";
    }
}
