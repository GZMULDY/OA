package com.bangong.process.service;

import com.bangong.model.process.ProcessType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProcessTypeService extends IService<ProcessType> {
        //获取审批分类与对应的审批模板
        List<ProcessType> findProcessType();
}
