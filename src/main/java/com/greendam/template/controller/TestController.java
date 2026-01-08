package com.greendam.template.controller;

import com.greendam.template.common.BaseResponse;
import com.greendam.template.common.utils.WechatUtils;
import com.greendam.template.common.entity.wechat.request.NewsMsg;
import com.greendam.template.common.entity.wechat.request.NewsArticle;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * 发送文本消息给所有用户（测试接口：使用假数据）
     */
    @GetMapping("/wechat/text")
    public BaseResponse<Void> sendTextMsgToAll() {
        wechatUtils.sendTextMsgToAll("测试文本消息");
        return BaseResponse.success();
    }

    /**
     * 发送图片消息给所有用户（测试接口：使用假 mediaId）
     */
    @GetMapping("/wechat/image")
    public BaseResponse<Void> sendImageMsgToAll() {
        wechatUtils.sendImageMsgToAll("MEDIA_ID_IMAGE");
        return BaseResponse.success();
    }

    /**
     * 发送语音消息给所有用户（测试接口：使用假 mediaId）
     */
    @GetMapping("/wechat/voice")
    public BaseResponse<Void> sendVoiceMsgToAll() {
        wechatUtils.sendVoiceMsgToAll("MEDIA_ID_VOICE");
        return BaseResponse.success();
    }

    /**
     * 发送视频消息给所有用户（测试接口：使用假 mediaId 和假标题/描述）
     */
    @GetMapping("/wechat/video")
    public BaseResponse<Void> sendVideoMsgToAll() {
        wechatUtils.sendVideoMsgToAll("MEDIA_ID_VIDEO", "测试视频标题", "测试视频描述");
        return BaseResponse.success();
    }

    /**
     * 发送文件消息给所有用户（测试接口：使用假 mediaId）
     */
    @GetMapping("/wechat/file")
    public BaseResponse<Void> sendFileMsgToAll() {
        wechatUtils.sendFileMsgToAll("MEDIA_ID_FILE");
        return BaseResponse.success();
    }

    /**
     * 发送 Markdown 消息给所有用户（测试接口：使用假数据）
     */
    @GetMapping("/wechat/markdown")
    public BaseResponse<Void> sendMarkdownMsgToAll() {
        wechatUtils.sendMarkdownMsgToAll("# 测试标题\n这是 Markdown 内容");
        return BaseResponse.success();
    }
}
