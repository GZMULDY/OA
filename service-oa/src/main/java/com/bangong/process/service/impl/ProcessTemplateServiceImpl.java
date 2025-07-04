package com.bangong.process.service.impl;

import com.bangong.model.process.ProcessTemplate;
import com.bangong.model.process.ProcessType;
import com.bangong.process.mapper.ProcessTemplateMapper;
import com.bangong.process.service.ProcessService;
import com.bangong.process.service.ProcessTemplateService;
import com.bangong.process.service.ProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper,ProcessTemplate> implements ProcessTemplateService {
        @Autowired
        private ProcessTemplateMapper processTemplateMapper;

        @Autowired
        private ProcessTypeService processTypeService;

        @Autowired
        private ProcessService processService;


    @Override
    public IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam) {
        LambdaQueryWrapper<ProcessTemplate> queryWrapper = new LambdaQueryWrapper<ProcessTemplate>();
        queryWrapper.orderByDesc(ProcessTemplate::getId);
        IPage<ProcessTemplate> page = processTemplateMapper.selectPage(pageParam, queryWrapper);
        List<ProcessTemplate> processTemplateList = page.getRecords();

        List<Long> processTypeIdList = processTemplateList.stream().map(processTemplate -> processTemplate.getProcessTypeId()).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(processTypeIdList)) {
            Map<Long, ProcessType> processTypeIdToProcessTypeMap = processTypeService.list(new LambdaQueryWrapper<ProcessType>().in(ProcessType::getId, processTypeIdList)).stream().collect(Collectors.toMap(ProcessType::getId, ProcessType -> ProcessType));
            for(ProcessTemplate processTemplate : processTemplateList) {
                ProcessType processType = processTypeIdToProcessTypeMap.get(processTemplate.getProcessTypeId());
                if(null == processType) continue;
                processTemplate.setProcessTypeName(processType.getName());
            }
        }
        return page;
    }


    @Transactional
    @Override
    public void publish(Long id) {
        ProcessTemplate processTemplate = this.getById(id);
        processTemplate.setStatus(1);
        processTemplateMapper.updateById(processTemplate);

        //优先发布在线流程设计
        if(!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())) {
            processService.deployByZip(processTemplate.getProcessDefinitionPath());
        }
    }
}
