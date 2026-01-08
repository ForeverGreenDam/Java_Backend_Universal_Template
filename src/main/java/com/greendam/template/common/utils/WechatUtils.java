package com.greendam.template.common.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.greendam.template.common.constant.WechatMsgType;
import com.greendam.template.common.entity.wechat.request.BaseMsg;
import com.greendam.template.common.entity.wechat.request.TextCardMsg;
import com.greendam.template.common.entity.wechat.request.TextMsg;
import com.greendam.template.common.entity.wechat.request.ImageMsg;
import com.greendam.template.common.entity.wechat.request.VoiceMsg;
import com.greendam.template.common.entity.wechat.request.VideoMsg;
import com.greendam.template.common.entity.wechat.request.FileMsg;
import com.greendam.template.common.entity.wechat.request.NewsMsg;
import com.greendam.template.common.entity.wechat.request.MarkdownMsg;
import com.greendam.template.common.entity.wechat.response.AccessTokenResponse;
import com.greendam.template.common.entity.wechat.response.SendMsgResponse;
import com.greendam.template.common.properties.WechatProperties;
import com.greendam.template.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.List;


@AllArgsConstructor
public class WechatUtils {

    private   WechatProperties wechatProperties;

    private static final String ACCESS_TOKEN_KEY = "wechat:access_token";

    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取微信access_token
     */
    public  String getAccessToken() {
        Object cacheObject = stringRedisTemplate.opsForValue().get(ACCESS_TOKEN_KEY);
        if (cacheObject != null) {
            return cacheObject.toString();
        }
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + wechatProperties.getCorpid() + "&corpsecret=" + wechatProperties.getSecret();
        String response = HttpUtil.get(url);
        AccessTokenResponse accessTokenResponse = JSONUtil.toBean(response, AccessTokenResponse.class);
        String accessToken = accessTokenResponse.getAccess_token();
        Long expiresIn = accessTokenResponse.getExpires_in();
        stringRedisTemplate.opsForValue().set(ACCESS_TOKEN_KEY, accessToken,Duration.ofSeconds(expiresIn-1000));
        return accessToken;
    }

    /**
     * 重新构建接收者列表, 将List转为|分隔的字符串
     * @param ids
     * @return
     */
    private  String rebuildToUserList(List<Long> ids){
        if(ids==null || ids.isEmpty()){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int size = ids.size();
        if (size == 1){
            return ids.get(0).toString();
        }else{
            sb.append(ids.get(0));
            for (int i = 1; i < size; i++) {
                sb.append("|").append(ids.get(i));
            }
            return sb.toString();
        }
    }
    /**
     * 为指定用户发送文本卡片消息
     * @param title
     * @param description
     * @param url
     * @param toUserList
     */
    public  void sendTextCardMsgToUsers(String title, String description, String url, List<Long> toUserList){
        sendTextCardMsg(title, description, url, rebuildToUserList(toUserList));
    }
    /**
     * 为所有用户发送文本卡片消息
     * @param title
     * @param description
     * @param url
     */
    public  void sendTextCardMsgToAll(String title, String description, String url){
        sendTextCardMsg(title, description, url, "@all");
    }
    /**
     * 发送文本卡片消息
     * @param title
     * @param description
     * @param url
     * @param toUser
     */
    private  void sendTextCardMsg(String title, String description, String url, String toUser){
        if (toUser == null){
            return;
        }
        TextCardMsg textCardMsg = new TextCardMsg();
        textCardMsg.setUrl(url);
        textCardMsg.setTitle(title);
        textCardMsg.setDescription(description);
        textCardMsg.setBtntxt("详情");
        send(toUser, WechatMsgType.TEXT_CARD, textCardMsg);
    }
    /**
     * 为指定用户发送文本消息
     * @param content 文本内容
     * @param toUserList 接收用户列表
     */
    public void sendTextMsgToUsers(String content, List<Long> toUserList) {
        sendTextMsg(content, rebuildToUserList(toUserList));
    }

    /**
     * 为所有用户发送文本消息
     * @param content 文本内容
     */
    public void sendTextMsgToAll(String content) {
        sendTextMsg(content, "@all");
    }

    /**
     * 发送文本消息
     * @param content 文本内容
     * @param toUser 接收者（"@all" 或 "id1|id2"）
     */
    private void sendTextMsg(String content, String toUser) {
        if (toUser == null) {
            return;
        }
        TextMsg textMsg = new TextMsg();
        textMsg.setContent(content);
        send(toUser, WechatMsgType.TEXT, textMsg);
    }

    /**
     * 为指定用户发送图片消息
     * @param mediaId 媒体文件 id
     * @param toUserList 接收用户列表
     */
    public void sendImageMsgToUsers(String mediaId, List<Long> toUserList) {
        sendImageMsg(mediaId, rebuildToUserList(toUserList));
    }

    /**
     * 为所有用户发送图片消息
     * @param mediaId 媒体文件 id
     */
    public void sendImageMsgToAll(String mediaId) {
        sendImageMsg(mediaId, "@all");
    }

    private void sendImageMsg(String mediaId, String toUser) {
        if (toUser == null) {
            return;
        }
        ImageMsg imageMsg = new ImageMsg();
        imageMsg.setMedia_id(mediaId);
        send(toUser, WechatMsgType.IMAGE, imageMsg);
    }

    /**
     * 为指定用户发送语音消息
     * @param mediaId 媒体文件 id
     * @param toUserList 接收用户列表
     */
    public void sendVoiceMsgToUsers(String mediaId, List<Long> toUserList) {
        sendVoiceMsg(mediaId, rebuildToUserList(toUserList));
    }

    /**
     * 为所有用户发送语音消息
     * @param mediaId 媒体文件 id
     */
    public void sendVoiceMsgToAll(String mediaId) {
        sendVoiceMsg(mediaId, "@all");
    }

    private void sendVoiceMsg(String mediaId, String toUser) {
        if (toUser == null) {
            return;
        }
        VoiceMsg voiceMsg = new VoiceMsg();
        voiceMsg.setMedia_id(mediaId);
        send(toUser, WechatMsgType.VOICE, voiceMsg);
    }

    /**
     * 为指定用户发送视频消息
     * @param mediaId 媒体文件 id
     * @param title 标题
     * @param description 描述
     * @param toUserList 接收用户列表
     */
    public void sendVideoMsgToUsers(String mediaId, String title, String description, List<Long> toUserList) {
        sendVideoMsg(mediaId, title, description, rebuildToUserList(toUserList));
    }

    /**
     * 为所有用户发送视频消息
     * @param mediaId 媒体文件 id
     * @param title 标题
     * @param description 描述
     */
    public void sendVideoMsgToAll(String mediaId, String title, String description) {
        sendVideoMsg(mediaId, title, description, "@all");
    }

    private void sendVideoMsg(String mediaId, String title, String description, String toUser) {
        if (toUser == null) {
            return;
        }
        VideoMsg videoMsg = new VideoMsg();
        videoMsg.setMedia_id(mediaId);
        videoMsg.setTitle(title);
        videoMsg.setDescription(description);
        send(toUser, WechatMsgType.VIDEO, videoMsg);
    }

    /**
     * 为指定用户发送文件消息
     * @param mediaId 媒体文件 id
     * @param toUserList 接收用户列表
     */
    public void sendFileMsgToUsers(String mediaId, List<Long> toUserList) {
        sendFileMsg(mediaId, rebuildToUserList(toUserList));
    }

    /**
     * 为所有用户发送文件消息
     * @param mediaId 媒体文件 id
     */
    public void sendFileMsgToAll(String mediaId) {
        sendFileMsg(mediaId, "@all");
    }

    private void sendFileMsg(String mediaId, String toUser) {
        if (toUser == null) {
            return;
        }
        FileMsg fileMsg = new FileMsg();
        fileMsg.setMedia_id(mediaId);
        send(toUser, WechatMsgType.FILE, fileMsg);
    }

    /**
     * 为指定用户发送图文消息
     * @param news 图文消息对象
     * @param toUserList 接收用户列表
     */
    public void sendNewsMsgToUsers(NewsMsg news, List<Long> toUserList) {
        sendNewsMsg(news, rebuildToUserList(toUserList));
    }

    /**
     * 为所有用户发送图文消息
     * @param news 图文消息对象
     */
    public void sendNewsMsgToAll(NewsMsg news) {
        sendNewsMsg(news, "@all");
    }

    private void sendNewsMsg(NewsMsg news, String toUser) {
        if (toUser == null || news == null) {
            return;
        }
        send(toUser, WechatMsgType.NEWS, news);
    }

    /**
     * 为指定用户发送 Markdown 消息
     * @param content markdown 内容
     * @param toUserList 接收用户列表
     */
    public void sendMarkdownMsgToUsers(String content, List<Long> toUserList) {
        sendMarkdownMsg(content, rebuildToUserList(toUserList));
    }

    /**
     * 为所有用户发送 Markdown 消息
     * @param content markdown 内容
     */
    public void sendMarkdownMsgToAll(String content) {
        sendMarkdownMsg(content, "@all");
    }

    private void sendMarkdownMsg(String content, String toUser) {
        if (toUser == null) {
            return;
        }
        MarkdownMsg markdownMsg = new MarkdownMsg();
        markdownMsg.setContent(content);
        send(toUser, WechatMsgType.MARKDOWN, markdownMsg);
    }
    /**
     * 发送消息
     * @param toUser
     * @param msg
     */
    private <T>  void send(String toUser, String msgType, T msg) {
        BaseMsg<T> baseMsg = BaseMsg.buildBaseMsg(msgType, msg, wechatProperties.getAgentId(), toUser);
        baseMsg.setSafe(0);
        String jsonWithMsgTypeKey = BaseMsg.toJsonWithMsgTypeKey(baseMsg);

        String accessToken = getAccessToken();
        String api = " https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+accessToken;
        String response = HttpUtil.post(api, jsonWithMsgTypeKey);
        SendMsgResponse sendMsgResponse = JSONUtil.toBean(response, SendMsgResponse.class);
        if("0".equals(sendMsgResponse.getErrcode())){
            return;
        }else{
            //access_token提前失效,重发
            stringRedisTemplate.delete(ACCESS_TOKEN_KEY);
            resend(baseMsg);
        }
    }


    /**
     * 重新发送消息,防止access_token提前失效
     * @param baseMsg
     */
    private  void resend(BaseMsg<?> baseMsg){
        String accessToken = getAccessToken();
        String api = " https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+accessToken;
        String response = HttpUtil.post(api, BaseMsg.toJsonWithMsgTypeKey(baseMsg));
        SendMsgResponse sendMsgResponse = JSONUtil.toBean(response, SendMsgResponse.class);
        if(!("0".equals(sendMsgResponse.getErrcode()))){
            throw new BusinessException(500, sendMsgResponse.getErrmsg());
        }
    }


}
