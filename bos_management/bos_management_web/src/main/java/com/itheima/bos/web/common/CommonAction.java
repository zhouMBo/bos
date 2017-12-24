package com.itheima.bos.web.common;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class CommonAction<T> extends ActionSupport implements ModelDriven<T>{
	private static final long serialVersionUID = 1L;
	/**
	 * 提供model对象
	 */
	private T model;
	public CommonAction() {
		//获取到具体子类的字节码
		Class<? extends CommonAction> clazz = this.getClass();
		//获取到父类的泛型数据
		Type genericSuperclass = clazz.getGenericSuperclass();
		//强制类型转换为ParameterizedType
		ParameterizedType type =  (ParameterizedType) genericSuperclass;
		//获取泛型参数组成的数组
		Type[] actualTypeArguments = type.getActualTypeArguments();
		//获取数组中的第一个
		Type childType = actualTypeArguments[0];
		//把获取到的type强制转换成字节码
		Class<T> childClazz = (Class<T>) childType;
		try {
			model = childClazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public T getModel() {
		return model;
	}
	
	
	//当前页数
	protected int page;
	public void setPage(int page) {
		this.page = page;
	}
	//一页显示的个数
	protected int rows;
	public void setRows(int rows) {
		this.rows = rows;
	}
	public void page2Json(Page<T> page, String[] excludes) throws IOException {
	     //获取总数据条数
	     long total = page.getTotalElements();
	     //获取当前也需要显示的数据
	     List<T> content = page.getContent();
	     //将total 和 rows 封装到map 
	     Map<String, Object> map = new HashMap<String,Object>();
	     map.put("total", total);
	     map.put("rows", content);
	     
         // 指定要忽略的字段
         JsonConfig config = new JsonConfig();
         config.setExcludes(excludes);
	     
	     //将map转换成json
	     String json = JSONObject.fromObject(map,config).toString();
 
	     //将json数据重定向到页面
	    HttpServletResponse response = ServletActionContext.getResponse();
	    //处理中文乱码
	    response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().write(json);
	}
	
	/**
	 * 将list数据转成json并响应到前端
	 * @param list
	 * @param excludes
	 * @throws IOException
	 */
	public void list2Json(List list, String[] excludes) throws IOException {
		// 指定要忽略的字段
        JsonConfig config = new JsonConfig();
        config.setExcludes(excludes);
	    //将list转换成json
	    String json = JSONArray.fromObject(list,config).toString();
	    //将json数据重定向到页面
	    HttpServletResponse response = ServletActionContext.getResponse();
	    //处理中文乱码
	    response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().write(json);
	}
	
}
