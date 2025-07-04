package com.bangong.auth.service;

import com.bangong.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface SysUserService extends IService<SysUser> {
    void updateStatus(Long id, Integer status);

    SysUser getByUsername(String username);

    Map<String, Object> getUserInfo(String username);

    Map<String, Object> getCurrentUser();
}
