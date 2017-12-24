package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.domain.base.Standard;


//JpaRepository<Standard, Long> -> 泛型一:操作对象的类型    泛型二:对象主键的类型
public interface StandardRepository extends JpaRepository<Standard, Long>{
	
	//通过姓名查找对象
	Standard findByName(String name);
	
	//根据用户名模糊查找用户
	List<Standard> findByNameLike(String name);
	
	//根据名字执行删除
	@Modifying    //表示要进行更新操作
	@Transactional
	@Query("delete from Standard where name = ?")
	void deleteByName(String name);
	
	//
	@Modifying
	@Transactional
	@Query("update Standard set maxLength = ? where name = ?")
	void updateMaxLengthByName(Integer maxLength,String name);
	

}
