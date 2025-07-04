package com.bangong.process.service;

import com.bangong.model.process.ProcessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProcessRecordService extends IService<ProcessRecord> {

    void record(Long processId, Integer status, String description);

}