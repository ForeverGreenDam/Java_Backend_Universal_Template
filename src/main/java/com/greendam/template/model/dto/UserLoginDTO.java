package com.greendam.template.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户
 * @author ForeverGreenDam
 * @TableName user
 */
@Data
public class UserLoginDTO implements Serializable {
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 验证码(如果有的话)
     */
    private String captcha;

    private static final long serialVersionUID = 1L;
}