package com.bangong.process.mapper;

import com.bangong.model.process.ProcessTemplate;
import com.bangong.model.process.ProcessType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcessTypeMapper extends BaseMapper<ProcessType> {
    
    /**
     * 根据审批类型ID查询对应的审批模板列表
     * @param typeId 审批类型ID
     * @return 审批模板列表
     */
    List<ProcessTemplate> selectProcessTemplatesByTypeId(@Param("typeId") Long typeId);
}
