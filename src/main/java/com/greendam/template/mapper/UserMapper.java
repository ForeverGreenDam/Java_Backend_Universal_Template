package com.greendam.template.mapper;

import com.greendam.template.model.entity.User;

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
    /**
     * 插入用户，要求 mapper 使用自增主键并回填 id
     * @param user 用户实体
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户（只更新非空字段）
     * @param user 用户实体
     * @return 影响行数
     */
    int update(User user);

    /**
     * 物理删除用户（谨慎使用）
     * @param id 用户 id
     * @return 影响行数
     */
    int deleteById(Long id);
}




