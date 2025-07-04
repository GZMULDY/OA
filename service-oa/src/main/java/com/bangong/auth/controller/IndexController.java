package com.bangong.auth.controller;

import com.bangong.auth.service.SysUserService;
import com.bangong.common.execption.BanGongException;
import com.bangong.common.jwt.JwtHelper;
import com.bangong.common.result.Result;
import com.bangong.common.utils.MD5;
import com.bangong.model.system.SysUser;
import com.bangong.vo.system.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    //login
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        SysUser sysUser = sysUserService.getByUsername(loginVo.getUsername());
        if(null == sysUser) {
            throw new BanGongException(201,"用户不存在");
        }
        if(!MD5.encrypt(loginVo.getPassword()).equals(sysUser.getPassword())) {
            throw new BanGongException(201,"密码错误");
        }
        if(sysUser.getStatus().intValue() == 0) {
            throw new BanGongException(201,"用户被禁用");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("token", JwtHelper.createToken(sysUser.getId(), sysUser.getUsername()));
        return Result.ok(map);
    }
    /**
     * 获取用户信息
     *
     * @return
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        String username = JwtHelper.getUsername(request.getHeader("token"));
        Map<String, Object> map = sysUserService.getUserInfo(username);
        return Result.ok(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}