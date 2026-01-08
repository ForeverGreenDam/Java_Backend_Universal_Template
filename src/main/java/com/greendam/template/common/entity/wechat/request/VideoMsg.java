package com.greendam.template.common.entity.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: VideoMsg
 * @Package: com.greendam.template.common.entity.wechat.request
 * @Description: 视频消息
 * @Author: ForeverGreenDam
 * @Create: 2026/1/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoMsg {

    /**
     * 视频消息媒体id，可以调用上传临时素材接口获取
     */
    private String media_id;
    /**
     * 视频消息的标题
     */
    private String title;
    /**
     * 视频消息的描述
     */
    private String description;

}
