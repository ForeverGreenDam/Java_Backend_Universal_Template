package com.greendam.template.common.entity.wechat.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: UploadMediaResponse
 * @Package: com.greendam.template.common.entity.wechat.response
 * @Description: 上传微信临时素材的响应类
 * @Author: ForeverGreenDam
 * @Create: 2026/01/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadMediaResponse {
    /**
     * 错误码
     */
    private String errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件（file）
     */
    private String type;

    /**
     * 媒体文件上传后获取的唯一标识，3天内有效
     */
    private String media_id;

    /**
     * 媒体文件上传时间戳
     */
    private String created_at;
}
