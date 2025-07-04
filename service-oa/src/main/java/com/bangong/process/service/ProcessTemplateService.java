package com.bangong.process.service;

import com.bangong.model.process.ProcessTemplate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProcessTemplateService extends IService<ProcessTemplate> {
    IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam);

    void publish(Long id);
}
