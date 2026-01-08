package com.greendam.template.common.entity.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @ClassName: TextCardMsg
 * @Package: com.greendam.template.common.entity.wechat.request
 * @Description: 文本卡片消息
 * @Author: ForeverGreenDam
 * @Create: 2026/1/8 13:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextCardMsg {
    /**
     * 标题，不超过128个字节，超过会自动截断
     */
    private String title;
    /**
     * 描述，不超过512个字节，超过会自动截断（支持id转译）
     */
    private String description;
    /**
     * 点击消息卡片后跳转的URL，最大长度 1024字节（请确保包含了协议头(http/https)）
     */
    private String url;
    /**
     * 按钮文字。 默认为“详情”， 不超过4个文字，超过自动截断。
     */
    private String btntxt;

}
