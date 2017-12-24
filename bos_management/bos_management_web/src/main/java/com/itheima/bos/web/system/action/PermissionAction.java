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

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.PermissionService;
import com.itheima.bos.web.common.CommonAction;
/**
 * @author 15443
 *处理与权限相关连的业务
 */
@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("struts-default")
public class PermissionAction extends CommonAction<Permission> {
	private static final long serialVersionUID = 2232508518659891027L;

	@Autowired
	private PermissionService permissionService;
	
	/**
	 * 保存权限
	 * @return
	 * @throws IOException
	 */
	@Action(value = "permissionAction_save", results = {@Result(name = "success",
            location = "/pages/system/permission.html", type = "redirect")})
	public String save() throws IOException {
		permissionService.save(getModel());
		return SUCCESS;
	}
	
	/**
	 * 分页查询所有权限
	 * @return
	 * @throws IOException
	 */
	@Action(value="permissionAction_pageQuery")
	public String pageQuery() throws IOException {
		// EasyUI的页码从1开始,SpringDataJPA框架构造PageRequest的时候,page是从0开始的
		Pageable pageable = new PageRequest(page -1, rows);
		Page<Permission> page = permissionService.pageQuery(pageable);
		page2Json(page, new String[]{"roles"});
		return NONE;
	}
	
	
	/**
	 * 查询所有权限
	 * @return
	 * @throws IOException
	 */
	@Action(value="permissionAction_findAll")
	public String findAll() throws IOException {
		Page<Permission> page = permissionService.pageQuery(null);
		List<Permission> list = page.getContent();
		list2Json(list, new String[]{"roles"});
		return NONE;
	}
}
