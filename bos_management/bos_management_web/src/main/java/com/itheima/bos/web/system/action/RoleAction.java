package com.itheima.bos.web.system.action;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;
import com.itheima.bos.web.common.CommonAction;
@Controller
@Namespace("/")
@Scope("scototype")
@ParentPackage("struts-default")
public class RoleAction extends CommonAction<Role> {
	private static final long serialVersionUID = -3561903668414552743L;

	@Autowired
	private RoleService roleService;
	/**
	 * 分页查询所有角色
	 * @return
	 * @throws IOException 
	 */
	@Action(value = "roleAction_pageQuery")
	public String pageQuery() throws IOException {
		Pageable pageable = new	PageRequest(page - 1, rows);
		Page<Role> page = roleService.pageQuery(pageable);
		page2Json(page, new String[]{"menus","permissions","users"});
		return NONE;
	}
	
	//获取菜单的id
	private String menuIds;
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	//获取权限的id
	private Long[] permissionIds;
	public void setPermissionIds(Long[] permissionIds) {
		this.permissionIds = permissionIds;
	}
	
	/**
	 * 保存新建角色
	 * @return
	 * @throws IOException
	 */

	@Action(value = "roleAction_save", results = {@Result(name = "success",
            location = "/pages/system/role.html", type = "redirect")})
	public String save() throws IOException {
		roleService.save(getModel(),menuIds,permissionIds);
		return SUCCESS;
	}
	
	
	/**
	 * 查询所有角色
	 * @return
	 * @throws IOException 
	 */
	@Action(value = "roleAction_findAll")
	public String findAll() throws IOException {
		Page<Role> page = roleService.pageQuery(null);
		List<Role> list = page.getContent();
		list2Json(list, new String[]{"menus","permissions","users"});
		return NONE;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
