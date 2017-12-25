package cn.leixin.service;

import cn.leixin.dao.Url;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface UrlService {
    //获取每个标签页
    void getURLInfo(String urlInfo,String charset,Integer page) throws Exception;
    //模糊查询视频
    List<Url> selectname(Url url);
    //查询视频
    List<Url> selectall();
    //获取avi视频地址
    List getAvi(String avi)throws Exception;
    //获取avi图片地址
    String getimg(String avi)throws Exception;
}
