package cn.leixin.dao;

import org.mybatis.spring.annotation.MapperScan;

import javax.naming.Name;

@MapperScan
public class Url {
    private Long id;
    private String url;
    private String name;
    private String urlavi;
    private String urlimg;

    public String getUrlimg() {
        return urlimg;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }

    public String getUrlavi() {
        return urlavi;
    }

    public void setUrlavi(String urlavi) {
        this.urlavi = urlavi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Url{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", urlavi='" + urlavi + '\'' +
                ", urlimg='" + urlimg + '\'' +
                '}';
    }
}
