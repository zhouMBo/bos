package com.itheima.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;
@Component
public class SmsConsumer implements MessageListener{

	@Override
	public void onMessage(Message msg) {
		MapMessage mapMessage = (MapMessage) msg;
		try {
			String tel = mapMessage.getString("tel");
			String code = mapMessage.getString("code");
			
			System.out.println(tel + "=======" + code);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
