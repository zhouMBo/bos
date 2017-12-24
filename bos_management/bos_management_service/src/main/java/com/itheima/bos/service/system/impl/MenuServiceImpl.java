package com.itheima.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService;
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	public List<Menu> findAllTopMenus() {
		return menuRepository.findByParentMenuIsNull();
	}

	@Override
	public void save(Menu model) {
		if (model.getParentMenu() != null && model.getParentMenu().getId() == null) {
			model.setParentMenu(null);
		}
		
		menuRepository.save(model);
	}

	@Override
	public Page<Menu> pageQuery(Pageable pageable) {
		return menuRepository.findAll(pageable);
	}

	@Override
	public List<Menu> findByUser(User user) {
		//超级管理员
		if (user.getUsername().equals("admin")) {
			return menuRepository.findAll();
		}
		//普通用户
		return menuRepository.findByUser(user.getId());
	}

}
