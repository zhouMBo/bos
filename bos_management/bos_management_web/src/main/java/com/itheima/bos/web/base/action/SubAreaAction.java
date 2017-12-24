package com.itheima.bos.web.base.action;

import java.io.IOException;

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

import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;
import com.itheima.bos.web.common.CommonAction;

@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class SubAreaAction extends CommonAction<SubArea> {
	private static final long serialVersionUID = -4537469947745439268L;
	
	@Autowired
	private SubAreaService subAreaService;

	/**
	 * 保存区域信息
	 * @return
	 */
	@Action(value = "subareaAction_save",results = {
			@Result(name ="success",location = "/pages/base/sub_area.html" ,type="redirect")})
	public String save() {
		subAreaService.save(getModel());
		return SUCCESS;
	}
	
	
	/**
	 * 分页查询
	 * @return
	 * @throws IOException
	 */
	@Action(value = "subareaAction_findByPage")
	public String findByPage() throws IOException {
		Pageable pageable =  new PageRequest(page - 1,rows);
		Page<SubArea> page = subAreaService.pageQuery(pageable);
		
		page2Json(page, new String[] {"subareas","couriers"});
		
		return NONE;
	}
	
}

