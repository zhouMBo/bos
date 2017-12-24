//包名中必须包含action,actions,struts,struts2中的一个/bos_management_service/src/main/java
package com.itheima.bos.web.base.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandardService;
import com.itheima.bos.web.common.CommonAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import freemarker.core.ReturnInstruction.Return;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Namespace("/")
@ParentPackage("struts-default")
@Controller  //代表本类是一个控制器,spring框架扫描到本注解后就会注册这个对象
@Scope("prototype")
public class StandardAction extends CommonAction<Standard>{
	@Autowired
	private StandardService standardService;
	
	
	/**
	 * 保存邮递标准
	 * value相当于action节点中的name属性
	 * @return
	 */
	@Action(value = "standardAction_save",results= {
		@Result(name="success",location="/pages/base/standard.html",type="redirect")	
	})
	public String save() {
		standardService.save(getModel());
		return SUCCESS;
	}
	
	/**
	 * 收派标准的分页查询
	 */
	@Action(value = "standardAction_pageQuery")
	public String pageQuery() throws IOException {
		// EasyUI的页码从1开始,SpringDataJPA框架构造PageRequest的时候,page是从0开始的
		Pageable pageable = new PageRequest(page - 1, rows);
		
	     Page<Standard> page =	standardService.pageQuery(pageable);
		
	    page2Json(page, null);
		return NONE;
	}
	
	@Action(value = "standardAction_findAll")
	public String  findAll() throws IOException {
		List<Standard> list = standardService.findAll();
		list2Json(list, null);
		return NONE                                                                                            ;
	}
}
