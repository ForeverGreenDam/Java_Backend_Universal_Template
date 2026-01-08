package com.greendam.template.common.config;


import com.greendam.template.common.properties.WechatProperties;
import com.greendam.template.common.utils.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @ClassName: WechatConfig
 * @Package: com.greendam.template.config
 * @Description: 微信配置类，用于创建微信工具类对象，方便在不同模块的上下文中使用
 * @Author: assistant
 * @Create: 2026/01/05
 */
@Configuration
@Slf4j
public class WechatConfig {
    @Bean
    @ConditionalOnMissingBean //确保只有一个WeChatUtil实例
    public WechatUtils wechatUtils(WechatProperties wechatProperties, StringRedisTemplate stringRedisTemplate) {
        log.info("开始创建微信工具类对象:{}", wechatProperties);
        return new WechatUtils(wechatProperties, stringRedisTemplate);
    }
}


