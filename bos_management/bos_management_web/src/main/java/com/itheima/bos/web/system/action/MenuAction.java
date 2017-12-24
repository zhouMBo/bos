package com.itheima.bos.web.system.action;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService;
import com.itheima.bos.web.common.CommonAction;


/**
 * @author 15443
 *处理与菜单相关业务
 */
@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("struts-default")
public class MenuAction extends CommonAction<Menu> {
	private static final long serialVersionUID = -7478060318460095911L;

	@Autowired
	private MenuService menuService;
	
	/**
	 * 查询所有的一级标签
	 * @return
	 * @throws IOException
	 */
	@Action(value = "menuAction_findAllTopMenus")
	public String findAllTopMenus() throws IOException {
		List<Menu> list = menuService.findAllTopMenus();
		list2Json(list, new String[] {"roles", "childrenMenus", "parentMenu"});
		return NONE;
	}
	
	/**
	 * 保存新建的标签
	 * @return
	 * @throws IOException
	 */
	@Action(value = "menuAction_save", results = {@Result(name = "success",
            location = "/pages/system/menu.html", type = "redirect")})
	public String save() throws IOException {
		menuService.save(getModel());
		return SUCCESS;
	}
	
	/**
	 * 分页查询所有菜单项
	 * @return
	 * @throws IOException
	 */
	@Action(value="menuAction_pageQuery")
	public String pageQuery() throws IOException {
		// EasyUI的页码从1开始,SpringDataJPA框架构造PageRequest的时候,page是从0开始的
		Pageable pageable = new PageRequest(Integer.parseInt(getModel().getPage()) -1, rows);
		Page<Menu> page = menuService.pageQuery(pageable);
		page2Json(page, new String[] {"roles", "childrenMenus", "parentMenu"});
		return NONE;
	}
	
	// 根据用户查找对应的菜单
	@Action(value = "menuAction_findByUser")
	public String findByUser() throws IOException {
		 // 获取用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		// 查询数据
		List<Menu> list = menuService.findByUser(user);
		list2Json(list, new String[] {"childrenMenus", "roles", "parentMenu","children"});
		return NONE;
	}
	
}
