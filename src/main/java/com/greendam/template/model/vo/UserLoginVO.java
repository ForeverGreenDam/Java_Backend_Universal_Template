package com.greendam.template.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @author ForeverGreenDam
 * @TableName user
 */
@Data
public class UserLoginVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 用户登录令牌
     */
    private String token;

    private static final long serialVersionUID = 1L;
}