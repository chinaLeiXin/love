package cn.leixin.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlDao {
   //新增数据
    Integer insert(Url url);
   //查询数据
    List<Url> selectname(Url url);
    //查询数据
    List<Url> selectall();
}
