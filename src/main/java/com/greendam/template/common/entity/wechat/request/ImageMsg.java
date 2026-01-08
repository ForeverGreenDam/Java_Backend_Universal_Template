package com.greendam.template.common.entity.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ImageMsg
 * @Package: com.greendam.template.common.entity.wechat.request
 * @Description: 图片消息
 * @Author: ForeverGreenDam
 * @Create: 2026/1/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageMsg {
    /**
     * 	图片媒体文件id，可以调用上传临时素材接口获取
     */
    private String media_id;
}
