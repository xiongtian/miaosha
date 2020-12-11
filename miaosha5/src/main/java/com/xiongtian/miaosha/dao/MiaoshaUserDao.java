package com.xiongtian.miaosha.dao;

import com.xiongtian.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author xiongtian
 * @Date 2020/12/2 22:30
 * @Version 1.0
 */

@Mapper
public interface MiaoshaUserDao {

    @Select("select * from miaosha_user where id=#{id}")
    public MiaoshaUser getById(@Param("id") Long id);

    @Update("update miaosha_user set password = #{password}  where id = #{id}")
    void update(MiaoshaUser toBeUpdate);
}
