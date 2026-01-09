package com.greendam.template.mapper;

import com.greendam.template.model.entity.TempFile;

/**
 * @author ForeverGreenDam
 * @description 针对表【temp_file(临时素材表)】的数据库操作Mapper
 * @createDate 2026-01-09
 */
public interface TempFileMapper {
    /**
     * 根据条件查询单条记录
     * @param tempFile 查询条件
     * @return TempFile
     */
    TempFile select(TempFile tempFile);

    /**
     * 插入记录，回填 id
     * @param tempFile 实体
     * @return 影响行数
     */
    int insert(TempFile tempFile);

    /**
     * 更新记录（只更新非空字段）
     * @param tempFile 实体
     * @return 影响行数
     */
    int update(TempFile tempFile);

    /**
     * 根据 id 物理删除
     * @param id id
     * @return 影响行数
     */
    int deleteById(Long id);
}


