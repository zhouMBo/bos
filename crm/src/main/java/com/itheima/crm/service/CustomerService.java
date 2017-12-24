package com.itheima.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import com.itheima.crm.domain.Customer;

public interface CustomerService {
	
	//查詢所有的客戶
	@GET
	@Path("/customer")
	@Produces({MediaType.APPLICATION_JSON})
	public List<Customer> findAll();
	
	//查询所有未关联到定区的客户
	@GET   //查询
	@Path("/findCustomersUnAssociated")//指定访问路径
	@Produces({MediaType.APPLICATION_JSON})//指定发送数据格式
	public List<Customer> findCustomersUnAssociated();

	//查询所有关联到当前定区的客户
	@GET
	@Path("/findCustomersAssociated2FixedArea")
	@Consumes(MediaType.APPLICATION_JSON) //指定接收数据的格式
	@Produces(MediaType.APPLICATION_JSON) //指定发送数据的格式
	public List<Customer> findCustomersAssociated2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId);

	// 关联客户到指定的定区
	@PUT
	@Path("/assignCustomers2FixedArea")
	@Consumes(MediaType.APPLICATION_JSON)
	public void assignCustomers2FixedArea(  @QueryParam("customerIds") List<Long> customerIds,
            @QueryParam("fixedAreaId") String fixedAreaId);


	//用户注册
	@POST
	@Path("/customer")
	@Consumes({MediaType.APPLICATION_JSON})
	public void save(Customer customer);
	
	// 通过手机号查找一个用户
	@GET
	@Path("/findByTelephone")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer findByTelephone(@QueryParam("telephone") String telephone);
	
	// 激活用户
	@PUT
	@Path("/activeCustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	public void activeCustomer(@QueryParam("telephone") String telephone);
	
	//登录
	@GET
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);
	
	// 根据客户地址查找定区ID
	@GET
	@Path("/findFixedAreaIdByAddress")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String findFixedAreaIdByAddress(@QueryParam("address") String address);
	
}
