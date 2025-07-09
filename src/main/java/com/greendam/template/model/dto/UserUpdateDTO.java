package com.greendam.template.model.dto;

import lombok.Data;

/**
 * 用户更新数据传输对象
 * @author ForeverGreenDam
 */
@Data
public class UserUpdateDTO implements java.io.Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 密码
     */
    private String userPassword;

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
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}
