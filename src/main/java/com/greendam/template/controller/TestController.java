package com.greendam.template.controller;

import com.greendam.template.common.BaseResponse;
import com.greendam.template.common.utils.WechatUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试接口
 * <p>此接口不会被jwt拦截
 * @author ForeverGreenDam
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private WechatUtils  wechatUtils;

    /**
     * 发送文本卡片消息给所有用户
     * @param title 标题
     * @param content 内容
     * @param url 跳转链接
     */
    @GetMapping("/wechat/textCard")
    public BaseResponse<Void> sendTextCardMsgToAll(String title, String content, String url) {
        wechatUtils.sendTextCardMsgToAll(title, content, url);
        return BaseResponse.success();
    }
}
