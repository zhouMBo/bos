package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Area;

public interface AreaRepository extends JpaRepository<Area, Long>{

	/**
	 * 根据条件查询区域信息
	 * @param q
	 * @return
	 */
	@Query("from Area where province like ?1 or city like ?1 "
			+ "or  district like ?1 or postcode like ?1 "
			+ "or citycode like ?1 or shortcode like ?1")
	List<Area> findByQ(String q);

	/**
	 * 通过省\市\区 查询 区域的详细信息
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 */
	Area findByProvinceAndCityAndDistrict(String province, String city, String district);

	/**
	 * 获取导出为HighCharts所需要的数据
	 * @return
	 */
	@Query("select a.province,count(*) from Area a group by a.province")
	List<Object[]> getHighChartsData();


}
