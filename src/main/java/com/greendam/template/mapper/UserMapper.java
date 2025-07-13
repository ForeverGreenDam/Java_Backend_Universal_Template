package com.greendam.template.mapper;

import com.greendam.template.model.entity.User;
import org.apache.ibatis.annotations.Select;

/**
* @author ForeverGreenDam
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2025-07-10 00:05:49
* @Entity com.greendam.template.model.entity.User
*/

public interface UserMapper {
    /**
     * 根据条件查询用户信息
     * @param user 用户对象，包含查询条件
     * @return User
     */
    User select(User user);
}




