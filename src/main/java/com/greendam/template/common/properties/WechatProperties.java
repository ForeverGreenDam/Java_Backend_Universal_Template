package com.greendam.template.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WechatProperties
 * @Package: com.greendam.template.properties
 * @Description: 微信配置属性
 * @Author: ForeverGreenDam
 * @Create: 2026/1/5 15:18
 */
@Data
@Component
@ConfigurationProperties(prefix = "your.wechat")
public class WechatProperties {
    /**
     * 应用ID
     */
    private String agentId;
    /**
     * 应用密钥
     */
    private String secret;
    /**
     * 公司ID
     */
    private String corpid;
}
