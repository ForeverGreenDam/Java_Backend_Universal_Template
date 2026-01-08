package com.greendam.template.common.entity.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: FileMsg
 * @Package: com.greendam.template.common.entity.wechat.request
 * @Description: 文件消息
 * @Author: ForeverGreenDam
 * @Create: 2026/1/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMsg {
    /**
     * 	文件id，可以调用上传临时素材接口获取媒体文件id
     */
    private String media_id;
}
