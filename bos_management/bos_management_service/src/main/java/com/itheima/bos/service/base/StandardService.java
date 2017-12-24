package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Standard;

public interface StandardService {

	/**
	 *保存收派标准 
	 * @param standard
	 */
	void save(Standard standard);

	/**
	 * 分页查询收派标准
	 * @param pageable
	 * @return
	 */
	Page<Standard> pageQuery(Pageable pageable);

	/**
	 * 查询所有的标准
	 * @return
	 */
	List<Standard> findAll();

}