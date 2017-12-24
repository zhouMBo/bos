 package com.itheima.bos.fore.web.action;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.itheima.crm.domain.Customer;
import com.itheima.utils.MailUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Namespace("/")
@Scope("scototype")
@ParentPackage("struts-default")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {
	private static final long serialVersionUID = 6131318023836916333L;

	private Customer model;
	@Override
	public Customer getModel() {
		if (model == null) {
			model = new Customer();
		}
		return model;
	}
	
	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * 发送短信
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Action(value = "customerAction_sendSMS")
	public String sendSMS() throws UnsupportedEncodingException {
		// 随机生成的验证码
		final String code = RandomStringUtils.randomNumeric(4);
		System.out.println("code的值为:"+code);
		//将code存储到session,将电话号码作为key
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), code);
		// 发送短信到消息中间件
		jmsTemplate.send("sms",new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("tel", model.getTelephone());
				mapMessage.setString("code", "尊敬的客户你好，您本次获取的验证码为：" + code);
				return mapMessage;
			}
		});
		return NONE;
	}
	
	/**
	 * 原始方式:
	 * 发送短信
	 * @return
	 * @throws UnsupportedEncodingException
	 */
/*	@Action(value = "customerAction_sendSMS")
	public String sendSMS() throws UnsupportedEncodingException {
		// 随机生成的验证码
		String code = RandomStringUtils.randomNumeric(4);
		System.out.println("code的值为:"+code);
		//将code存储到session,将电话号码作为key
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), code);
		//发送短信
		SmsUtils.sendSmsByHTTP(model.getTelephone(), "尊敬的客户你好，您本次获取的验证码为：" + code);
		return NONE;
	}*/
	
	/**
	 * 注册账户
	 */
	//获取短信验证码
	private String checkcode;
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	//获取redis模板
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Action(value = "customerAction_regist",
		results = {
            @Result(name = "success", location = "/signup-success.html",
                    type = "redirect"),
            @Result(name = "error", location = "/signup-fail.html",
                    type = "redirect")})
	public String regist() {
		// 获取Session中的验证码
		String servletCode = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
		
		//判断验证码是否为空  且  session中的验证码是否为空  且  session中的验证码与前端提交过来的验证码是否相等
		if(StringUtils.isNotEmpty(checkcode)&&StringUtils.isNotEmpty(servletCode)&&servletCode.equals(checkcode)) {
			 // 发起WebService请求注册用户
			WebClient
			.create("http://localhost:8180/crm/webservice/cs/customer")
			.type(MediaType.APPLICATION_JSON)
			.post(model);
			
			// 动态生成激活码
			String activeCode = RandomStringUtils.randomNumeric(32);
			//将激活码存储到redis中,将手机号码做为key,防止被覆盖
			redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 1, TimeUnit.DAYS);
			// 发送一封激活邮件
			String emailBody =  "感谢您注册速运快递,请点击<a href='http://localhost:8280/bos_fore/customerAction_active.action?activeCode="
                    + activeCode + "&telephone=" + model.getTelephone()
                    + "'>本链接</a>,激活您的帐号";
			MailUtils.sendMail(model.getEmail(), "激活邮件", emailBody);
			
			return SUCCESS;
		}
		return ERROR;
	}
	
	//获取激活码
	private String activeCode;
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	/**
	 * 激活用户
	 * @return
	 */
	 @Action(value = "customerAction_active",
        results = {
            @Result(name = "success", location = "/signup-success.html",
                    type = "redirect"),
            @Result(name = "actived", location = "/actived.html",
                    type = "redirect"),
            @Result(name = "error", location = "/signup-fail.html",
                    type = "redirect")})
	  public String active() {
		  // 获取用户的手机号
		 String telephone = model.getTelephone();
		  // 比较激活码
		 String redisCode = redisTemplate.opsForValue().get(telephone);
		  //判断redis中的激活码不为空   且    前端提交过来的激活码不为空    且    两者相等
		 if (StringUtils.isNotEmpty(redisCode)&&StringUtils.isNoneEmpty(activeCode)&&redisCode.equals(activeCode)) {
			 // 发起WebService请求根据手机号查找用户
			 Customer customer = WebClient
			 	.create("http://localhost:8180/crm/webservice/cs/findByTelephone")
			 	.query("telephone", telephone)
			 	.accept(MediaType.APPLICATION_JSON)
			 	.type(MediaType.APPLICATION_JSON)
			 	.get(Customer.class);
			 
			// 用户已经激活,直接提示用户去登录
			 if (customer != null && customer.getType() != null && customer.getType() == 1) {
				return "actived";
			}
			//激活用户
			 WebClient
			 	.create("http://localhost:8180/crm/webservice/cs/activeCustomer")
			 	.query("telephone", telephone)
			 	.type(MediaType.APPLICATION_JSON)
			 	.put(null);
			 return SUCCESS;
		}
		 return ERROR;
	 	}

	 /**
	 * 处理登录业务 
	 * @return
	 */
	@Action(value = "customerAction_login" ,
        results = {
            @Result(name = "success", location = "/index.html",type = "redirect"),
            @Result(name = "need_active", location = "/actived.html",type = "redirect"),
            @Result(name = "error", location = "/signup-fail.html",type = "redirect")
            })
	public String login() {
		//获取存在session中的验证码
		String servletValidateCode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
		//判断验证码
		if (StringUtils.isNotEmpty(servletValidateCode)&&StringUtils.isNotEmpty(checkcode)&&checkcode.equals(servletValidateCode)) {
			 // 比对手机号和密码
			Customer customer = WebClient
				.create("http://localhost:8180/crm/webservice/cs/login")
				.query("telephone", model.getTelephone())
				.query("password", model.getPassword())
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get(Customer.class);
			//判断用户是否激活
			if (customer != null && customer.getType() != null && customer.getType() == 1) {
				//已激活,将customer添加到session
				ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
				return SUCCESS;
			}
			return "need_active";
		}
		return ERROR;
	 }
	 
	 
	 
	 
	 
}
