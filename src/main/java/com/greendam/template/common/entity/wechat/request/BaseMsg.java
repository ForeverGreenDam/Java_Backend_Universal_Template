package com.greendam.template.common.entity.wechat.request;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @ClassName: BaseMsg
 * @Package: com.greendam.template.common.entity.wechat.request
 * @Description: 微信消息基本请求类
 * @Author: ForeverGreenDam
 * @Create: 2026/01/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMsg<T> {
    /**
     * 接收者，多个接收者用‘|’分隔，最多支持1000个。
     * 当touser为"@all"时忽略本参数
     */
    private String touser;
    /**
     * 接收者部门，多个接收者用‘|’分隔，最多支持100个。
     * 当touser为"@all"时忽略本参数
     */
    private String toparty;
    /**
     * 接收者标签，多个接收者标签用‘|’分隔，最多支持100个。
     * 当touser为"@all"时忽略本参数
     */
    private String totag;
    /**
     * 消息类型
     */
    private String msgtype;
    /**
     * 企业应用的id，整型。企业内部开发，可在应用的设置页面查看；第三方服务商，可通过接口 获取企业授权信息 获取该参数值
     */
    private String agentid;
    /**
     * 消息内容,属性名为msgtype对应的消息类型，例如：text为文本消息，需要在序列化时手动替换
     */
    private T replaceName;
    /**
     * 表示是否是保密消息，0表示可对外分享，1表示不能分享且内容显示水印，默认0
     */
    private Integer safe;
    /**
     * 表示是否开启id转译，0表示否，1表示是，默认0。
     */
    private Integer enable_id_trans;
    /**
     * 表示是否开启重复消息检查，0表示否，1表示是，默认0。
     */
    private Integer enable_duplicate_check;
    /**
     * 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时
     */
    private Integer duplicate_check_interval;


    public static <T> String toJsonWithMsgTypeKey(BaseMsg<T> baseMsg) {
        if (baseMsg == null) {
            return null;
        }

        // 先将对象转换为JSON字符串
        String jsonStr = JSONUtil.toJsonStr(baseMsg);

        // 获取msgtype的值，用于替换replaceName为实际的消息类型
        String msgtype = baseMsg.getMsgtype();
        if (msgtype != null && !msgtype.isEmpty()) {
            // 将replaceName替换为msgtype的实际值
            jsonStr = jsonStr.replace("\"replaceName\"", "\"" + msgtype + "\"");
        }

        return jsonStr;
    }

    public static <T> BaseMsg<T> buildBaseMsg(String msgtype, T msg, String agentid, String touser) {
        BaseMsg<T> baseMsg = new BaseMsg<>();
        baseMsg.setMsgtype(msgtype);
        baseMsg.setReplaceName(msg);
        baseMsg.setAgentid(agentid);
        baseMsg.setTouser(touser);
       return baseMsg;
    }
//    {
//        "touser" : "UserID1|UserID2|UserID3",
//        "toparty" : "PartyID1 | PartyID2",
//        "totag" : "TagID1 | TagID2",
//        "msgtype" : "textcard",
//        "agentid" : 1,
//        "textcard" : {
//        "title" : "领奖通知",
//            "description" : "<div class=\"gray\">2016年9月26日</div> <div class=\"normal\">恭喜你抽中iPhone 7一台，领奖码：xxxx</div><div class=\"highlight\">请于2016年10月10日前联系行政同事领取</div>",
//            "url" : "URL",
//            "btntxt":"更多"
//    },
//        "enable_id_trans": 0,
//        "enable_duplicate_check": 0,
//        "duplicate_check_interval": 1800
//    }

}
