package com.itheima.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.dao.base.FixedAreaRepositroy;
import com.itheima.bos.dao.take_delivery.OrderRepository;
import com.itheima.bos.dao.take_delivery.WorkBillRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.service.take_delivery.OrderService;
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private FixedAreaRepositroy fixedAreaRepositroy;
	@Autowired
	private WorkBillRepository workBillRepository;
	
	@Override
	public void saveOrder(Order order) {
		// 把瞬时态的sendArea和recArea变成持久态的对象
		
		//这个sendArea是前台的Action中直接new出来的对象
		Area sendArea = order.getSendArea();
		if(sendArea != null) {
			// sendAreaDB是从数据库查询出来的对象
			//订单中需要区域的详细信息,所以需要从数据库中查出区域的详细信息来(area)
			Area sendAreaDB = areaRepository.findByProvinceAndCityAndDistrict(
				sendArea.getProvince(),sendArea.getCity(),sendArea.getDistrict());
			
			order.setSendArea(sendAreaDB);
		}
	
		Area recArea = order.getRecArea();
		if (recArea != null) {
			Area RecAreaDB = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(),recArea.getCity(),recArea.getDistrict());
			order.setRecArea(RecAreaDB);
		}
		
		//保存订单
		order.setOrderNum(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
		order.setOrderTime(new Date());
		orderRepository.save(order);
		
		/**
		 * 自动分单方法一:
		 * 		根据客户填写的详细地址去CRM中匹配定区ID,实现自动分单;
		 * 		在CRM中有对应的客户的详细信息,而且该客户关联了对应的定区;
		 */
		// 获取用户在下单页面填写的详细发件地址
		String sendAddress = order.getSendAddress();
		
		if (StringUtils.isNotEmpty(sendAddress)) {
			// 调用CRM系统查询地址对应的定区ID
			String fixedAreaId = WebClient
				.create("http://localhost:8180/crm/webservice/cs/findFixedAreaIdByAddress")
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.query("address", sendAddress)
				.get(String.class);
			
			//判断定区ID是否为空,不为空进入自动分单方法一
			if (StringUtils.isNotEmpty(fixedAreaId)) {
				//获取定区对象
				FixedArea fixedArea = fixedAreaRepositroy.findOne(Long.parseLong(fixedAreaId));
				//获取与当前定区绑定的所有快递员
				Set<Courier> couriers = fixedArea.getCouriers();
				if (!couriers.isEmpty()) {
					Iterator<Courier> iterator = couriers.iterator();
					Courier courier = iterator.next();
					//为订单指定快递员
					order.setCourier(courier);
					//手动封装工单(workBill)
					WorkBill workBill = new WorkBill();
					workBill.setAttachbilltimes(0);
					workBill.setBuildtime(new Date());
					workBill.setCourier(courier);
					workBill.setOrder(order);
					workBill.setPickstate("新单");
					workBill.setRemark(order.getRemark());
					workBill.setSmsNumber("123");
					workBill.setType("新");
					
					//保存工单
					workBillRepository.save(workBill);
					System.out.println("有一个新的工单");
					//将在order中的订单类型设置为自动分单
					order.setOrderType("自动分单");
					
					//中断代码执行
					return;
				}
			}
		}
		/**
		 * 自动分单方法二:
		 * 		使用分区关键字进行自动分单;
		 * 		用户在选择发件区域的时候,对应的区域必须在数据库中存在;
		 * 		遍历区域中的所有的分区,分区的关键字和辅助关键字获取出来,比对地址;
		 */
		Area sendAreaDB = order.getSendArea();
		//获取与定区相关联的所有分区
		Set<SubArea> subareas = sendAreaDB.getSubareas();
		for (SubArea subArea : subareas) {
			//获取关键字
			String keyWords = subArea.getKeyWords();
			//获取辅助关键字
			String assistKeyWords = subArea.getAssistKeyWords();
			// 如果客户填写的地址包含关键字或者辅助关键字,那我们就认为找到了对应的分区
			if (sendAddress.contains(keyWords) || sendAddress.contains(assistKeyWords)) {
				//找到与分区相关连的定区
				FixedArea fixedArea = subArea.getFixedArea();
				//获取与定区相关连的所有快递员
				Set<Courier> couriers = fixedArea.getCouriers();
				if (!couriers.isEmpty()) {
					Iterator<Courier> iterator = couriers.iterator();
					Courier courier = iterator.next();
					// 为订单指定快递员
                    order.setCourier(courier);
                    WorkBill workBill = new WorkBill();
                    workBill.setAttachbilltimes(0);
                    workBill.setBuildtime(new Date());
                    workBill.setCourier(courier);
                    workBill.setOrder(order);
                    workBill.setPickstate("新单");
                    workBill.setRemark(order.getRemark());
                    workBill.setSmsNumber("123");
                    workBill.setType("新");
                    // 保存工单
                    workBillRepository.save(workBill);
                    System.out.println("有一个新的工单");

                    order.setOrderType("自动分单");
                    // 中断代码的执行
                    return;
				}
			}
		}
		/**
		 * 当上述两种方法都无法自动分单时,采用人工分单
		 */
		order.setOrderType("人工分单");
	}

}
