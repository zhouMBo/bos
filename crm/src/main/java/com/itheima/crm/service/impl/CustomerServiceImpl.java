package com.itheima.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.crm.dao.CustomerRepository;
import com.itheima.crm.domain.Customer;
import com.itheima.crm.service.CustomerService;
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	//查詢所有的客戶
	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}
	
	//查询所有未关联到定区的客户
	@Override
	public List<Customer> findCustomersUnAssociated() {
		return customerRepository.findByFixedAreaIdIsNull();
	}
	
	//查询所有关联到当前定区的客户
	@Override
	public List<Customer> findCustomersAssociated2FixedArea(String fixedAreaId) {
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	// 关联客户到指定的定区
	@Override
	public void assignCustomers2FixedArea(List<Long> customerIds,String fixedAreaId) {
		if (StringUtils.isNotEmpty(fixedAreaId)){
			 // 把关联到指定定区的客户,全部解绑
			customerRepository.unbindFixedAreaById(fixedAreaId);
			// 重新绑定
			for (Long id : customerIds) {
				customerRepository.bindCustomer2FixedArea(fixedAreaId,id);
			}
		}
	}

	/**
	 * 保存新注册用户
	 */
	@Override
	public void save(Customer customer) {
		customerRepository.save(customer);
	}

	/**
	 * 通过手机号码查找用户
	 */
	@Override
	public Customer findByTelephone(String telephone) {
		return customerRepository.findByTelephone(telephone);
	}

	/**
	 * 激活用户
	 */
	@Override
	public void activeCustomer(String telephone) {
		customerRepository.activeCustomer(telephone);
	}

	/**
	 * 登录
	 */
	@Override
	public Customer login(String telephone, String password) {
		return customerRepository.findByTelephoneAndPassword(telephone,password);
	}

	/**
	 *  根据客户地址查找定区ID
	 */
	@Override
	public String findFixedAreaIdByAddress(String address) {
		return customerRepository.findFixedAreaIdByAddress(address);
	}
}
