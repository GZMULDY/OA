package com.bangong.auth.service;

import com.bangong.model.system.SysRole;
import com.bangong.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface SysRoleService extends IService<SysRole> {
    /**
     * 根据用户获取角色数据
     * @param userId
     * @return
     */
    Map<String, Object> findRoleByAdminId(Long userId);

    /**
     * 分配角色
     * @param assginRoleVo
     */
    void doAssign(AssginRoleVo assginRoleVo);
}
