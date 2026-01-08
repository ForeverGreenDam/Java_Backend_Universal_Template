package com.greendam.template.common.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.greendam.template.common.entity.wechat.request.BaseMsg;
import com.greendam.template.common.entity.wechat.request.TextCardMsg;
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

        BaseMsg<TextCardMsg> baseMsg = new BaseMsg<>();
        baseMsg.setMsgtype("textcard");
        baseMsg.setTouser(toUser);
        baseMsg.setReplaceName(textCardMsg);
        baseMsg.setAgentid(wechatProperties.getAgentId());
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
