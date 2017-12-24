package com.itheima.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepositroy;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.crm.domain.Customer;
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService{

	@Autowired
	private FixedAreaRepositroy  fixedAreaRepositroy;
	
	@Override
	public void save(FixedArea fixedArea) {
		fixedAreaRepositroy.save(fixedArea);
	}

	@Override
	public Page<FixedArea> pageQuery(Pageable pageable) {
		return fixedAreaRepositroy.findAll(pageable);
	}
	@Autowired
	private CourierRepository courierRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	
	//把快递员关联到定区
	@Override
	public void associationCourierToFixedArea(Long id, Long courierId, Long takeTimeId) {
		
		// 持久态的对象才有更新数据的能力
		// 持久态的定区
		FixedArea fixedArea = fixedAreaRepositroy.findOne(id);
		// 持久态的快递员
		Courier courier = courierRepository.findOne(courierId);
		// 持久态的时间
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		
		// 建立快递员和时间之间的关系
		courier.setTakeTime(takeTime);
		 // 建立快递员和定区之间的关系
		fixedArea.getCouriers().add(courier);
	}

	/**
	 * 查询所有没有关联定区的分区
	 */
	@Autowired
	private SubAreaRepository subAreaRepository;
	
	@Override
	public List<SubArea> findUnAssociatedSubarea() {
		return subAreaRepository.findByFixedAreaIsNull();
	}

	/**
	 * 根据id查询对应的定区
	 */
	@Override
	public FixedArea findById(Long id) {
		return fixedAreaRepositroy.findOne(id);
	}

	/**
	 * 关联分区到定区
	 * 分区和定区的关系维护只能由分区来完成, 因为在定区使用了mappedBy属性,放弃了对两者关系的维护
	 */
	@Override
	public void assignSubArea2FixedArea(List<Long> subareaIds, Long id) {
		if(id != null && id != 0) {
			  // 根据ID获取定区
			FixedArea fixedArea = fixedAreaRepositroy.findOne(id);
			 // 获取和定区关联的分区
			Set<SubArea> subareas = fixedArea.getSubareas();
			 // 将分区和定区解绑
			for (SubArea subArea : subareas) {
				subArea.setFixedArea(null);
			}
			// 遍历分区ID,根据ID查找持久态的分区,使用持久态的分区关联定区
			for (Long subareaId : subareaIds) {
				SubArea subArea = subAreaRepository.findOne(subareaId);
				subArea.setFixedArea(fixedArea);
			}
		}
	}

	/**
	 * 根据定区id查询与该定区相关的所有快递员
	 */
	@Override
	public List<Courier> findCourierByFixedAreaId(Long id) {
		return fixedAreaRepositroy.findCourierById(id);
	}

}
