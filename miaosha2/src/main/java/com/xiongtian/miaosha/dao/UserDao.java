package com.xiongtian.miaosha.dao;

import com.xiongtian.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from user where id= #{id}")
    public User getUserById(@Param("id") int id);

    @Insert("insert into user(id,name) values(#{user.id},#{user.name}) ")
    public int insert(@Param("user") User user);
}
