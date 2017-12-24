package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;

public interface FixedAreaRepositroy extends JpaRepository<FixedArea, Long> {

	/**
	 * 根据定区id查询与该定区相关的所有快递员
	 */
	@Query("select c from Courier c inner join c.fixedAreas f  where f.id = ?")
	List<Courier> findCourierById(Long id);


}
