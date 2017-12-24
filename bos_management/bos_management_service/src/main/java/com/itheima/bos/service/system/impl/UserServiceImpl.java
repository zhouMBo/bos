package com.itheima.bos.service.system.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.dao.system.UserRepository;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public Page<User> pageQuery(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
	@Override
	public void save(User user, Long[] roleIds) {
		userRepository.save(user);
		
		if (roleIds != null && roleIds.length > 0) {
			for (Long roleId : roleIds) {
				//获取持久化的role
				Role role = roleRepository.findOne(roleId);
				user.getRoles().add(role);
			}
		}
	}

}
