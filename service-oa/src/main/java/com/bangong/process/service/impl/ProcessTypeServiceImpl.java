package com.bangong.process.service.impl;

import com.bangong.model.process.ProcessTemplate;
import com.bangong.model.process.ProcessType;
import com.bangong.process.mapper.ProcessTypeMapper;
import com.bangong.process.service.ProcessTemplateService;
import com.bangong.process.service.ProcessTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType> implements ProcessTypeService {

    private final ProcessTemplateService processTemplateService;

    public ProcessTypeServiceImpl(ProcessTemplateService processTemplateService) {
        this.processTemplateService = processTemplateService;
    }

    @Override
    public List<ProcessType> findProcessType() {
        //1遍历所有审批分类，返回list集合
        List<ProcessType> processTypeList = baseMapper.selectList(null);

        //2遍历返回的所有审批分类集合
        for (ProcessType processType : processTypeList) {
            //3得到每个审批分类，根据审批分类ID查询对应的模板
            //审批分类ID
            Long TypeId = processType.getId();
            LambdaQueryWrapper<ProcessTemplate> wrapper = new LambdaQueryWrapper();
            wrapper.eq(ProcessTemplate::getProcessTypeId, TypeId);
            List<ProcessTemplate> processTemplateList = processTemplateService.list(wrapper);

            //4根据审批分类ID查询对应审批模板数据（list）封装到每个审批分类对象里
            processType.setProcessTemplateList(processTemplateList);

        }

        return processTypeList;

    }
}