package com.bangong.auth.service.impl;

import com.bangong.auth.mapper.SysUserMapper;
import com.bangong.auth.service.SysMenuService;
import com.bangong.auth.service.SysUserService;
import com.bangong.model.system.SysUser;
import com.bangong.security.custom.LoginUserInfoHelper;
import com.bangong.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysMenuService sysMenuService;


    @Transactional
    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser sysUser = this.getById(id);
        if (status.intValue() == 1) {
            sysUser.setStatus(status);
        } else {
            sysUser.setStatus(0);
        }
        this.updateById(sysUser);
    }

    @Override
    public SysUser getByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> result = new HashMap<>();
        SysUser sysUser = this.getByUsername(username);

        //根据用户id获取菜单权限值
        List<RouterVo> routerVoList = sysMenuService.findUserMenuList(sysUser.getId());
        //根据用户id获取用户按钮权限
        List<String> permsList = sysMenuService.findUserPermsList(sysUser.getId());

        result.put("name", sysUser.getName());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        //当前权限控制使用不到，我们暂时忽略
        result.put("roles",  new HashSet<>());
        result.put("buttons", permsList);
        result.put("routers", routerVoList);
        return result;
    }

    @Override
    public Map<String, Object> getCurrentUser() {
        SysUser sysUser = baseMapper.selectById(LoginUserInfoHelper.getUserId());
        //SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        //SysPost sysPost = sysPostService.getById(sysUser.getPostId());
        Map<String, Object> map = new HashMap<>();
        map.put("name", sysUser.getName());
        map.put("phone", sysUser.getPhone());
        //map.put("deptName", sysDept.getName());
        //map.put("postName", sysPost.getName());
        return map;
    }
}