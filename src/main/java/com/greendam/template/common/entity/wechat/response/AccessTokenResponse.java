package com.greendam.template.common.entity.wechat.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @ClassName: AccessTokenResponse
 * @Package: com.greendam.template.common.entity.wechat.response
 * @Description: 获取微信接口访问凭证的响应类
 * @Author: ForeverGreenDam
 * @Create: 2026/01/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessTokenResponse {
    /**
     * 错误码
     */
    private String errcode;
    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 访问凭证
     */
    private String access_token;
    /**
     * 访问凭证有效时间，单位：秒
     */
    private Long expires_in;
}
