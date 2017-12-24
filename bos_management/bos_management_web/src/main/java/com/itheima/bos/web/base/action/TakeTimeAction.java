package com.itheima.bos.web.base.action;

import java.io.IOException;
import java.util.List;

import javax.inject.Named;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.TakeTimeService;
import com.itheima.bos.web.common.CommonAction;

@Controller
@Scope("scototype")
@Namespace("/")
@ParentPackage("struts-default")
public class TakeTimeAction extends CommonAction<TakeTime> {
	private static final long serialVersionUID = 5911908251641198427L;

	@Autowired
	private TakeTimeService takeTimeService;

	//查询所有的时间
	@Action(value = "takeTimeAction_findAll")
	public String findAll() throws IOException {
		List<TakeTime> list = takeTimeService.findAll();
	
		list2Json(list, null);
		return NONE;
	}
}
