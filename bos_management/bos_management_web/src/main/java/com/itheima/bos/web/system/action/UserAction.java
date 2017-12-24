package com.itheima.bos.web.system.action;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
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

import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.UserService;
import com.itheima.bos.web.common.CommonAction;
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class UserAction extends CommonAction<User> {
	private static final long serialVersionUID = 3153493968426880377L;

	@Autowired 
	private UserService userService;
	
	//获取前端传送过来的验证码
	private String checkCode;
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
	@Action(value = "userAction_login",
            results = {
                    @Result(name = "success", location = "/index.html",type = "redirect"),
                    @Result(name = "login", location = "/login.html",type = "redirect")})
	public String login() {
		//获取存在session中的验证码
		String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
		
		if (StringUtils.isNotEmpty(serverCode) && StringUtils.isNotEmpty(checkCode) && serverCode.equals(checkCode)) {
			//登录交给shiro框架处理
			Subject subject = SecurityUtils.getSubject();
			//创建令牌
			AuthenticationToken token = new UsernamePasswordToken(getModel().getUsername(),getModel().getPassword());
			
			try {
				//登录
				subject.login(token);
				//把用户存入session
				User user = (User) subject.getPrincipal();
				ServletActionContext.getRequest().getSession().setAttribute("user",user);
				return SUCCESS;
			} catch (AuthenticationException e) {
				e.printStackTrace();
			}
		}
		return LOGIN;
	}
	
	
	@Action(value = "userAction_logout",results = {@Result(name = "login", location = "/login.html", type = "redirect")})
	public String logout() {
		//调用logout方法注销subject
		SecurityUtils.getSubject().logout();
		//将session中的user清除
		ServletActionContext.getRequest().getSession().invalidate();
		return LOGIN;
	}
	
	/**
	 * 分页查询所有角色
	 * @return
	 * @throws IOException 
	 */
	@Action(value = "userAction_pageQuery")
	public String pageQuery() throws IOException {
		Pageable pageable = new	PageRequest(page - 1, rows);
		Page<User> page = userService.pageQuery(pageable);
		page2Json(page, new String[]{"roles"});
		return NONE;
	}
	
	//获取角色的id
	private Long[] roleIds;
	
	/**
	 * 保存新建角色
	 * @return
	 * @throws IOException
	 */
	@Action(value = "userAction_save", results = {@Result(name = "success",
            location = "/pages/system/userlist.html", type = "redirect")})
	public String save() throws IOException {
		userService.save(getModel(),roleIds);
		return SUCCESS;
	}
}
