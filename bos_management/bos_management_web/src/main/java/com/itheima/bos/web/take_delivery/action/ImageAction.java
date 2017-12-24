package com.itheima.bos.web.take_delivery.action;

import java.io.File;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.web.common.CommonAction;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class ImageAction extends CommonAction<Permission> {
	private static final long serialVersionUID = 4779878856114781472L;
	
	//使用属性驱动获取文件对象
	private File imgFile;
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	
	//属性驱动获取文件名
	private String imgFileFileName;
	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}
	
	
	@Action(value = "imageAction_upload")
	public String imageAction_upload() {
		
		return NONE;
	}
}
