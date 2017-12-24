package com.itheima.bos.service.jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.take_delivery.WorkBillRepository;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.utils.MailUtils;

@Component
public class WorkbillJob {
	
	@Autowired
	private WorkBillRepository workBillRepository;
	
	/**
	 * 定时用邮件发送工单信息
	 */
	public void sendMail() {
		//查询所有的工单
		List<WorkBill> list = workBillRepository.findAll();
		
		String emailBody = "编号\t类型\t取件状态\t快递员<br/>";
		for (WorkBill workBill : list) {
			emailBody += workBill.getId() + "\t" + workBill.getType() + "\t"
                    + workBill.getPickstate() + "\t"
                    + workBill.getCourier().getName() + "<br/>";
		}
		MailUtils.sendMail("ls@store.com", "工单信息统计", emailBody);
		
		System.out.println("邮件发送成功");
	}
}
