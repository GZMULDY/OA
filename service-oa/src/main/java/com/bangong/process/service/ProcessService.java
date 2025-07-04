package com.bangong.process.service;

import com.bangong.model.process.Process;
import com.bangong.vo.process.ApprovalVo;
import com.bangong.vo.process.ProcessFormVo;
import com.bangong.vo.process.ProcessQueryVo;
import com.bangong.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface ProcessService extends IService<Process> {
    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);
    //部署流程定义
    void deployByZip(String deployPath);

    void startUp(ProcessFormVo processFormVo);

    IPage<ProcessVo> findPending(Page<Process> pageParam);

    //审批详情接口
    Map<String, Object> show(Long id);

    //审批接口
    void approve(ApprovalVo approvalVo);

    //已处理接口
    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    //已发起接口
    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
