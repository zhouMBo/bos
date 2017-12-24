package com.itheima.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

	@Autowired
	private SubAreaRepository subAreaRepository;

	@Override
	public void save(SubArea subArea) {
		subAreaRepository.save(subArea);		
	}

	@Override
	public Page<SubArea> pageQuery(Pageable pageable) {
		return subAreaRepository.findAll(pageable);
	}
	

	
}
