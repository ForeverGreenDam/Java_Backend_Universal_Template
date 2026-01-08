package com.greendam.template.common.entity.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: NewsMsg
 * @Package: com.greendam.template.common.entity.wechat.request
 * @Description: 图文消息
 * @Author: ForeverGreenDam
 * @Create: 2026/1/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsMsg {

    /**
     * 图文消息
     */
    List<NewsArticle> articles;

}
