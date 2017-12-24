package com.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	/**
	 * 查找所有的一级菜单
	 * @return
	 */
	List<Menu> findByParentMenuIsNull();

	@Query("select m from Menu m inner join m.roles r inner join r.users u where u.id = ?")
	List<Menu> findByUser(Long uid);

}
