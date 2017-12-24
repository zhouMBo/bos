package com.itheima.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.system.User;

public interface UserRepository extends JpaRepository<User, Long>{
	//通过用户名查找用户
	User findByUsername(String username);
}
