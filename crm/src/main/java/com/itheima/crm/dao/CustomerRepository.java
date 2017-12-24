package com.itheima.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	//查询所有未关联到定区的客户
	List<Customer> findByFixedAreaIdIsNull();
	//查询所有关联到当前定区的客户
	List<Customer> findByFixedAreaId(String fixedAreaId);
	
	//将客户与定区解绑
	@Modifying
	@Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
	void unbindFixedAreaById(String fixedAreaId);

	//将客户与当前定区绑定
	@Modifying
	@Query("update Customer set fixedAreaId = ? where id = ?")
	void bindCustomer2FixedArea(String fixedAreaId, Long id);
	
	//通过手机号码查找用户
	Customer findByTelephone(String telephone);
	
	//激活用户
	@Modifying
	@Query("update Customer set type = 1 where telephone = ?")
	void activeCustomer(String telephone);
	
	//登录 ----- 通过电话和密码查询用户
	Customer findByTelephoneAndPassword(String telephone, String password);
	
	// 根据客户地址查找定区ID
	@Query("select fixedAreaId from Customer where address = ?")
	String findFixedAreaIdByAddress(String address);
}
