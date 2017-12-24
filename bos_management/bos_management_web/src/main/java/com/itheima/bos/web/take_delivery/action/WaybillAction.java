package com.itheima.bos.web.take_delivery.action;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WaybillService;
import com.itheima.bos.web.common.CommonAction;

@Controller
@Namespace("/")
@Scope("scototype")
@ParentPackage("struts-default")
public class WaybillAction extends CommonAction<WayBill> {
	private static final long serialVersionUID = 264398262395831027L;

	@Autowired
	private WaybillService waybillService;
	
	@Action(value = "waybillAction_save")
	public String save() throws IOException {
		String flag = "1";
		
		try {
			waybillService.save(getModel());
		} catch (Exception e) {
			flag = "0";
			e.printStackTrace();
		}
		ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().write(flag);
		return NONE;
	}
	
	
}
