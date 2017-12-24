package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.crm.domain.Customer;

public interface FixedAreaService {

	void save(FixedArea fixedArea);

	Page<FixedArea> pageQuery(Pageable pageable);


	/**
	 * 将快递员与定区绑定
	 * @param model
	 * @param courierId
	 * @param takeTimeId
	 */
	void associationCourierToFixedArea(Long id, Long courierId, Long takeTimeId);

	/**
	 * 查询所有没有关联定区的分区
	 * @return
	 */
	List<SubArea> findUnAssociatedSubarea();

	/**
	 * 根据id查询对应的定区
	 * @param id
	 * @return
	 */
	FixedArea findById(Long id);

	/**
	 * 关联分区
	 * @param subareaIds
	 * @param id
	 */
	void assignSubArea2FixedArea(List<Long> subareaIds, Long id);

	/**
	 * @param id
	 * @return
	 * 根据定区id查询与该定区相关的所有快递员
	 */
	List<Courier> findCourierByFixedAreaId(Long id);

}
