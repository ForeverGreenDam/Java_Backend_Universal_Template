package com.greendam.template.common.entity.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图文消息中的单篇文章对象（公共类）
 * @author ForeverGreenDam
 * @create 2026/01/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticle {
    /**
     * 图文消息标题
     */
    private String title;
    /**
     * 图文消息描述
     */
    private String description;
    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80
     */
    private String picurl;
    /**
     * 点击图文消息跳转链接
     */
    private String url;
    /**
     * 跳转的appid
     */
    private String appid;
    /**
     * 跳转的页面路径
     */
    private String pagepath;
}


