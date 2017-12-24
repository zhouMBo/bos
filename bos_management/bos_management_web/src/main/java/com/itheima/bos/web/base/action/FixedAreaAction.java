package com.itheima.bos.web.base.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.cxf.jaxrs.client.WebClient;
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

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.bos.web.common.CommonAction;
import com.itheima.crm.domain.Customer;

/**
 * 负责与定区相关的方法
 * @author 15443
 */
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("scototype")
public class FixedAreaAction extends CommonAction<FixedArea> {
	private static final long serialVersionUID = -296656013674551936L;

	@Autowired
	private FixedAreaService fixedAreaService;
	
	@Action( value = "fixedAreaAction_save" ,
			results = {@Result(name = "success" ,location = "/pages/base/fixed_area.html" ,type = "redirect")})
	public String save() {
		fixedAreaService.save(getModel());
		return SUCCESS;
	}
	
	@Action(value = "fixedAreaAction_pageQuery")
	public String pageQuery() throws IOException {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<FixedArea> page = (Page<FixedArea>) fixedAreaService.pageQuery(pageable);
		
		page2Json(page, new String[] {"subareas","couriers"});
		return NONE;
	}
	
	 // 获取未关联到定区的客户
	@Action(value = "fixedAreaAction_findUnAssociatedCustomers")
	public String findUnAssociatedCustomers() throws IOException {
		List<? extends Customer> list = (List<? extends Customer>) WebClient
		.create("http://localhost:8180/crm/webservice/cs/findCustomersUnAssociated")
		.accept(MediaType.APPLICATION_JSON)
		.getCollection(Customer.class);
		
		list2Json(list, null);
		return NONE;
	}
	
	// 获取到用户选择的定区的ID
	@Action("fixedAreaAction_findCustomersAssociated2FixedArea")
	public String  findCustomersAssociated2FixedArea() throws IOException {
		List<? extends Customer> list = (List<? extends Customer>) WebClient
			.create("http://localhost:8180/crm/webservice/cs/findCustomersAssociated2FixedArea")
			.query("fixedAreaId",getModel().getId())
			.accept(MediaType.APPLICATION_JSON)
			.getCollection(Customer.class);
		
		list2Json(list, null);
		return NONE;
	}
	//获取customerIds
	private List<Long> customerIds;
	public void setCustomerIds(List<Long> customerIds) {
		this.customerIds = customerIds;
	}
	
	/**
	 * 关联客户到指定的定区
	 * @return
	 */
	@Action(value = "fixedAreaAction_assignCustomers2FixedArea", results = {@Result(name = "success",
            location = "/pages/base/fixed_area.html",type = "redirect")})
	public String assignCustomers2FixedArea() {
		WebClient
		.create("http://localhost:8180/crm/webservice/cs/assignCustomers2FixedArea")
		.query("fixedAreaId", getModel().getId())//当前定区id
		.query("customerIds", customerIds)//客户id
		.accept(MediaType.APPLICATION_JSON)
		.put(null);
		return SUCCESS;
	}
	
	//获取快递员id
	private Long courierId;
	public void setCourierId(Long courierId) {
		this.courierId = courierId;
	}
	//获取收派时间takeTime
	private Long takeTimeId;
	public void setTakeTimeId(Long takeTimeId) {
		this.takeTimeId = takeTimeId;
	}
	//关联定区与快递员
	@Action(value = "fixedAreaAction_associationCourierToFixedArea", results = {@Result(name = "success",
            location = "/pages/base/fixed_area.html",type = "redirect")})
	public String associationCourierToFixedArea(){
		fixedAreaService.associationCourierToFixedArea(getModel().getId(),courierId,takeTimeId);
		return SUCCESS;
	}
	
	/**
	 * 查询所有没有关联定区的分区
	 * @return
	 * @throws IOException 
	 */
	@Action(value = "fixedAreaAction_findUnAssociatedSubarea")
	public String findUnAssociatedSubarea() throws IOException {
	   List<SubArea> list = fixedAreaService.findUnAssociatedSubarea();
	   list2Json(list, new String[] {"subareas"});//new String[] {"subareas"}
		return NONE;
	}
	
	/**
	 * 根据前端传过来的id查询当前定区下的所有分区
	 * @return
	 * @throws IOException
	 */
	@Action(value = "fixedAreaAction_findSubAreasAssociated2FixedArea")
	public String findSubAreasAssociated2FixedArea() throws IOException {
	  //根据id查询对应的定区
	   FixedArea fixedArea = fixedAreaService.findById(getModel().getId());
	  //获取本定区下的所有分区
	   Set<SubArea> subareas = fixedArea.getSubareas();
       // 将set集合转为list集合
       List<SubArea> list = new ArrayList<>(subareas);

	   list2Json(list, new String[] {"area", "fixedArea"});
		return NONE;
	}
	
	//获取所有分区id
	private List<Long> subareaIds;
	public void setSubareaIds(List<Long> subareaIds) {
		this.subareaIds = subareaIds;
	}
	/**
	 * 关联分区
	 * @return
	 * @throws IOException
	 */
	@Action(value = "fixedAreaAction_associationSubarea2FixedArea",results = {@Result(name = "success",
            location = "/pages/base/fixed_area.html",type = "redirect")})
	public String associationSubarea2FixedArea() throws IOException {
		fixedAreaService.assignSubArea2FixedArea(subareaIds,getModel().getId());
		return SUCCESS;
	}
	
    
    /**
     * @return
     * @throws IOException
     * 根据定区id查询与该定区相关的所有快递员
     */
    @Action("fixedAreaAction_findCourierByFixedAreaId")
    public String findCourierByFixedAreaId() throws IOException {
    	List<Courier> list = fixedAreaService.findCourierByFixedAreaId(getModel().getId());
    	list2Json(list, new String[] {"fixedAreas"});
    	return NONE;
    }
    
    /**
	 * 根据前端传过来的id查询当前定区下的所有分区
	 * @return
	 * @throws IOException
	 */
	@Action(value = "fixedAreaAction_findSubareaByFixedAreaId")
	public String fixedAreaAction_findSubareaByFixedAreaId() throws IOException {
	  //根据id查询对应的定区
	   FixedArea fixedArea = fixedAreaService.findById(getModel().getId());
	  //获取本定区下的所有分区
	   Set<SubArea> subareas = fixedArea.getSubareas();
       // 将set集合转为list集合
       List<SubArea> list = new ArrayList<>(subareas);

	   list2Json(list, new String[] {"subareas", "fixedArea"});
		return NONE;
	}

	
}
