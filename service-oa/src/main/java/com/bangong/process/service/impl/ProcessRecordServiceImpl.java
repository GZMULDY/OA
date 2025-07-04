package com.bangong.process.service.impl;

import com.bangong.auth.service.SysUserService;
import com.bangong.model.process.ProcessRecord;
import com.bangong.model.system.SysUser;
import com.bangong.process.mapper.ProcessRecordMapper;
import com.bangong.process.service.ProcessRecordService;
import com.bangong.security.custom.LoginUserInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord> implements ProcessRecordService {

    @Autowired
    private ProcessRecordMapper processRecordMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void record(Long processId, Integer status, String description) {
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUserId(sysUser.getId());
        processRecord.setOperateUser(sysUser.getName());
        processRecordMapper.insert(processRecord);
    }
}
