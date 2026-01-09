package com.greendam.template.service.impl;

import com.greendam.template.common.utils.WechatUtils;
import com.greendam.template.mapper.TempFileMapper;
import com.greendam.template.model.entity.TempFile;
import com.greendam.template.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 文件服务实现类
 *
 * @author ForeverGreenDam
 * @date 2026/01/09
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private WechatUtils wechatUtils;

    @Autowired
    private TempFileMapper tempFileMapper;

    @Override
    public String uploadWechatTempFile(byte[] fileBytes, String filename, String type) {
        // 调用 WechatUtils 上传文件到微信服务器
        String mediaId = wechatUtils.uploadFile(fileBytes, filename, type);

        // 保存上传记录到数据库
        TempFile tempFile = new TempFile();
        tempFile.setMediaId(mediaId);
        tempFile.setFileName(filename);
        tempFile.setFileSize((long) fileBytes.length);
        tempFile.setFileType(type);
        tempFile.setIsDelete(0);
        tempFile.setUploadTime(new Date());

        tempFileMapper.insert(tempFile);

        return mediaId;
    }

    @Override
    public String uploadWechatTempFile(MultipartFile file, String type) {
        try {
            // MultipartFile.getBytes() 返回文件的原始字节数据
            // 对于二进制文件，这是正确的原始字节
            // 对于UTF-8编码的文本文件，字节数据也是正确的
            byte[] fileBytes = file.getBytes();
            return uploadWechatTempFile(fileBytes, file.getOriginalFilename(), type);
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage(), e);
        }
    }

    @Override
    public String uploadWechatTempTextFile(String textContent, String filename) {
        // 确保文本内容按UTF-8编码转换为字节数组
        byte[] textBytes = textContent.getBytes(StandardCharsets.UTF_8);
        return uploadWechatTempFile(textBytes, filename, "file");
    }
}
