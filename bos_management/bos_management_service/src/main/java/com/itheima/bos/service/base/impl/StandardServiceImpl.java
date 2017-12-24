package com.itheima.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {

	//注入dao
	@Autowired
	private StandardRepository standardRepository;

	/**
	 * 保存新收派标准
	 */
	@Override
	public void save(Standard standard) {
		standardRepository.save(standard);
	}

	/**
	 * 分页查询收派标准
	 */
	@Override
	public Page<Standard> pageQuery(Pageable pageable) {
		Page<Standard> page = standardRepository.findAll(pageable);
		return page;
	}

	/**
	 * 查询所有的标准
	 */
	@Override
	public List<Standard> findAll() {
		return standardRepository.findAll();
		
	}
}
