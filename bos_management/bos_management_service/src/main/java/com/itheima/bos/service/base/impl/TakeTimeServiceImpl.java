package com.itheima.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.TakeTimeService;

@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService{

	@Autowired
	private TakeTimeRepository takeTimeRepository;
	
	/**
	 * 查询所有的时间
	 */
	@Override
	public List<TakeTime> findAll() {
		return takeTimeRepository.findAll();
	}

}
