package com.greendam.template.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 *
 * @author ForeverGreenDam
 * @date 2026/01/09
 */
public interface FileService {

    /**
     * 上传微信临时素材文件
     *
     * @param fileBytes 文件字节数组
     * @param filename 文件名
     * @param type 文件类型：image/voice/video/file
     * @return 返回 media_id
     */
    String uploadWechatTempFile(byte[] fileBytes, String filename, String type);

    /**
     * 上传微信临时素材文件（MultipartFile版本）
     *
     * @param file 上传的文件
     * @param type 文件类型：image/voice/video/file
     * @return 返回 media_id
     */
    String uploadWechatTempFile(MultipartFile file, String type);

    /**
     * 上传微信临时素材文本文件（确保UTF-8编码）
     *
     * @param textContent 文本内容
     * @param filename 文件名
     * @return 返回 media_id
     */
    String uploadWechatTempTextFile(String textContent, String filename);
}
