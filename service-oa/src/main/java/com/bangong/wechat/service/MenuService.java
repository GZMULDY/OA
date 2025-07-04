package com.bangong.wechat.service;

import com.bangong.model.wechat.Menu;
import com.bangong.vo.wechat.MenuVo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface MenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();

}