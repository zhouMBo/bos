package com.itheima.bos.service.system.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.dao.system.PermissionRepository;
import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	/**
	 *  分页查询所有角色
	 */
	@Override
	public Page<Role> pageQuery(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}
	/**
	 * 保存新建角色
	 * 只能使用角色关联菜单和权限,因为菜单和权限的实体类中声明了,对关系的放弃
	 */
	@Override
	public void save(Role role, String menuIds, Long[] permissionIds) {
		//保存新建角色
		roleRepository.save(role);
		//关联role与menu
		if (StringUtils.isNotEmpty(menuIds)) {
			String[] smenuId = menuIds.split(",");
			for (String menuId : smenuId) {
				//获取持久化的Menu
				Menu menu = menuRepository.findOne(Long.parseLong(menuId));
				role.getMenus().add(menu);
			}
		}
		//关联role与permission
		if (permissionIds != null && permissionIds.length > 0) {
			for (Long permissionId : permissionIds) {
				//获取持久化的permission
				Permission permission = permissionRepository.findOne(permissionId);
				role.getPermissions().add(permission);
			}
		}
	}

}
