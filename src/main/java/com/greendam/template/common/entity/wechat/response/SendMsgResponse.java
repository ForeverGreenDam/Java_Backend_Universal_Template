package com.greendam.template.common.entity.wechat.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @ClassName: SendMsgResponse
 * @Package: com.greendam.template.common.entity.wechat.response
 * @Description: 发送消息响应类
 * @Author: ForeverGreenDam
 * @Create: 2026/01/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMsgResponse {
    /**
     * 错误码 , 0表示成功，其他值表示失败
     */
    private String errcode;
    /**
     * 错误信息
     */
    private String errmsg;
    /**
     * 非法的userid列表，即在接收者列表中存在，但用户不存在
     */
    private String invaliduser;
    /**
     * 非法的部门id列表，即在接收者列表中存在，但部门不存在
     */
    private String invalidparty;
    /**
     * 非法的标签id列表，即在接收者列表中存在，但标签不存在
     */
    private String invalidtag;
    /**
     * 非法的userid列表，即在接收者列表中存在，但用户不存在
     */
    private String unlicenseduser;
    /**
     * 消息id
     */
    private String msgid;
    /**
     * 响应码
     */
    private String response_code;
//    {
//        "errcode" : 0,
//        "errmsg" : "ok",
//        "invaliduser" : "userid1|userid2",
//        "invalidparty" : "partyid1|partyid2",
//        "invalidtag": "tagid1|tagid2",
//        "unlicenseduser" : "userid3|userid4",
//        "msgid": "xxxx",
//        "response_code": "xyzxyz"
//    }

}
