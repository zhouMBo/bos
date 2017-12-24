package com.itheima.bos.web.base.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.CourierService;
import com.itheima.bos.web.common.CommonAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CourierAction extends CommonAction<Courier>{
	private static final long serialVersionUID = -8516978422975375934L;
	//邮递员

	@Autowired
	private CourierService courierService;
	//保存邮递员
	@Action(value = "courierAction_save",results = {@Result(name = "success",
			location = "/pages/base/courier.html",type = "redirect")})
	public String save() {
		courierService.save(getModel());
		return SUCCESS;
	}
	
	
	/**
	 * 分页查新邮递员信息
	 * @return
	 */
	@Action(value = "courierAction_pageQuery")
	public String pageQuery() throws IOException {
		//动态构造查询条件
		Specification<Courier> specification = new  Specification<Courier>() {
			//构造查询条件
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//root:根对象,可理解为泛型对象       query:不使用这个对象       cb:用来构造查询条件的对象
				ArrayList<Predicate> list = new ArrayList<>();
				
				//获取四个查询对象
				String courierNum = getModel().getCourierNum();
				String company = getModel().getCompany();
				String type = getModel().getType();
				Standard standard = getModel().getStandard();
				
				//如果快递员编号不为空,则构造一个等值查询
				if(StringUtils.isNotEmpty(courierNum)) {
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class),courierNum);
					list.add(p1);
				}
				//如果公司不为空,构造一个模糊查询
				if(StringUtils.isNotEmpty(company)) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+company+"%");
					list.add(p2);
				}
				//如果快递员类型不为空,构造等值查询
				if(StringUtils.isNotEmpty(type)) {
					Predicate p3 = cb.equal(root.get("type").as(String.class), type);
					list.add(p3);
				}
				//如果收派标准不为空,构造等值查询
				if (standard != null) {
					String name = standard.getName();
					if (StringUtils.isNotEmpty(name)) {
						//Join<Object, Object> join = root.join("standard");
						Predicate p4 = cb.equal(root.join("standard").get("name").as(String.class), name);
						list.add(p4);
					}
				}
				//如果用户没有输入任何查询条件,返回null
				if (list.size() == 0) {
					return null;
				}
				
				//当构建多个add条件时需要使用数组
				Predicate[] arr = new Predicate[list.size()];
				list.toArray(arr);
				//构建add查询条件
				return cb.and(arr);
				//return cb.and(list.toArray(new Predicate[list.size()]));
			}
		};
	
		Pageable pageable =  new PageRequest(page - 1,rows);
		Page<Courier> page = courierService.pageQuery(specification,pageable);
		
		page2Json(page, new String[] {"fixedAreas", "takeTime"});
		return NONE;
	}
	
	private String ids;
	public void setIds(String ids) {
        this.ids = ids;
    }
	
	// 删除快递员
    @Action(value = "courierAction_batchDel",
            results = {@Result(name = "success",
                    location = "/pages/base/courier.html", type = "redirect")})
	public String batchDel() {
		courierService.batchDel(ids);
		return SUCCESS;
	}
    
    //查询所有快递员,为关联快递员提供选择
    @Action(value = "courierAction_findAll")
    public String findAll() throws IOException {
    	List<Courier> list = courierService.findAll();
    	list2Json(list, new String[] {"fixedAreas"});
    	return NONE;
    }
	
}
