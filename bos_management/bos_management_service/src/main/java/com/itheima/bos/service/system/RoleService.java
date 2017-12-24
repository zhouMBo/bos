package com.itheima.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Role;

public interface RoleService {

	/**
	 *  分页查询所有角色
	 * @param pageable
	 * @return
	 */
	Page<Role> pageQuery(Pageable pageable);

	/**
	 * 保存新建角色
	 * @param model
	 * @param menuIds
	 * @param permissionIds
	 */
	void save(Role role, String menuIds, Long[] permissionIds);

}
