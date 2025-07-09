package com.greendam.template.model.dto;

import lombok.Data;

/**
 * 用户注册数据传输对象
 * @author ForeverGreenDam
 */
@Data
public class UserRegisterDTO implements java.io.Serializable {
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 重复密码
     */
    private String userPasswordConfirm;

    /**
     * 验证码(如果有的话)
     */
    private String captcha;

    private static final long serialVersionUID = 1L;
}
