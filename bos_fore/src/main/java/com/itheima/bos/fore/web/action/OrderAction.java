package com.itheima.bos.fore.web.action;


import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class OrderAction extends ActionSupport implements ModelDriven<Order> {
	private static final long serialVersionUID = -1728932647939396117L;

	private Order model;
	@Override
	public Order getModel() {
		if(model == null) {
			model = new Order();
		}
		return model;
	}
	// 使用属性驱动获取发件地址和收件地址
	private String sendAreaInfo;
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}
	private String recAreaInfo;
	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	@Action(value = "orderAction_saveOrder",results = {@Result(name = "success", location = "/index.html",
            type = "redirect")})
	public String  saveOrder() {
		//封装发件地址
		if (StringUtils.isNotEmpty(sendAreaInfo)) {
			//切割省市区
			String[] split = sendAreaInfo.split("/");
			String province = split[0];
			String city = split[1];
			String district = split[2];
			//切割去掉最后一个字符
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);
			//封装为Area对象
	    	Area area = new Area();
	    	area.setProvince(province);
	    	area.setCity(city);
	    	area.setDistrict(district);
	    	
	    	model.setSendArea(area);
		}
		//封装收件地址
		if (StringUtils.isNotEmpty(recAreaInfo)) {
			 // 切割省市区
            String[] split = recAreaInfo.split("/");
            String province = split[0];
            String city = split[1];
            String district = split[2];
            // 切掉最后一个字符
            province = province.substring(0, province.length() - 1);
            city = city.substring(0, city.length() - 1);
            district = district.substring(0, district.length() - 1);
            // 封装为Area对象
            Area area = new Area();
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);

            model.setRecArea(area);
		}
		//调用webService让后台完成保存订单
		WebClient
			.create("http://localhost:8080/bos_management_web/webservice/os/order")
			.type(MediaType.APPLICATION_JSON)
			.post(model);
		
		return SUCCESS;
	}

}
