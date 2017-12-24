package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.bos.domain.base.Courier;

public interface CourierService {

	/**
	 * 保存邮递员信息
	 * @param model
	 */
	void save(Courier courier);

	/**
	 * 分页查询快递员
	 * @param pageable
	 * @return
	 */
	Page<Courier> pageQuery(Specification<Courier> specification,Pageable pageable);

	/**
	 * 批量删除快递员
	 * @param ids
	 */
	void batchDel(String ids);

	
	/**
	 * 查询所有快递员
	 * @return
	 */
	List<Courier> findAll();

}
