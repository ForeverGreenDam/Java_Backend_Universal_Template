package com.greendam.template.common.entity.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: MarkdownMsg
 * @Package: com.greendam.template.common.entity.wechat.request
 * @Description: MD消息
 * @Author: ForeverGreenDam
 * @Create: 2026/1/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkdownMsg {
    /**
     * 消息内容，最长不超过2048个字节，超过将截断（支持id转译）
     */
    private String content;
}
