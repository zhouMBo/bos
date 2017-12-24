package com.itheima.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.User;

public interface UserService {

	Page<User> pageQuery(Pageable pageable);

	void save(User user, Long[] roleIds);

}
