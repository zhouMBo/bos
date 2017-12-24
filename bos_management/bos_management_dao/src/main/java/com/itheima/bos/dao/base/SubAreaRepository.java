package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.base.SubArea;

public interface SubAreaRepository extends JpaRepository<SubArea, Long>{

	
	/**
	 * 查询除所有没有关联定区的分区
	 * @return
	 */
	List<SubArea> findByFixedAreaIsNull();



}
