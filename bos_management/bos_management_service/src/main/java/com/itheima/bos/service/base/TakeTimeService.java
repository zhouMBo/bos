package com.itheima.bos.service.base;

import java.util.List;

import com.itheima.bos.domain.base.TakeTime;

public interface TakeTimeService {

	//查询所有的时间
	List<TakeTime> findAll();

}
