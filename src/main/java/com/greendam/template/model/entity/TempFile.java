package com.greendam.template.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 临时素材表实体
 * @author ForeverGreenDam
 */
@Data
public class TempFile implements Serializable {
    private Long id;

    /**
     * 微信媒体ID
     */
    private String mediaId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型/媒体类型（image/voice/video/file）
     */
    private String fileType;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}


