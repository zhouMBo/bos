package com.itheima.bos.service.system;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;

public interface MenuService {

	/**
	 * 查询所有一级菜单栏
	 * @return
	 */
	List<Menu> findAllTopMenus();

	/**
	 * 保存菜单栏
	 * @param model
	 */
	void save(Menu model);

	/**
	 * 分页查询菜单栏
	 * @param pageable
	 * @return
	 */
	Page<Menu> pageQuery(Pageable pageable);

	List<Menu> findByUser(User user);

}
