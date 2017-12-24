package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Area;

public interface AreaService {

	/**
	 * 保存分区数据
	 * @param list
	 */
	void save(List<Area> list);

	/**
	 * 分页查询区域
	 * @param pageable
	 * @return
	 */
	Page<Area> pageQuery(Pageable pageable);

	/**
	 * 查询所有区域
	 * @return
	 */
	List<Area> findAll();

	/**
	 * 通过条件q查询区域信息
	 * @param q
	 * @return
	 */
	List<Area> findByQ(String q);

	/**
	 * @return
	 * 获取导出为HighCharts所需要的数据
	 */
	List<Object[]> getHighChartsData();

}
