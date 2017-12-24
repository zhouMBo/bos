package com.itheima.bos.web.base.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.web.common.CommonAction;
import com.itheima.utils.FileDownloadUtils;
import com.itheima.utils.PinYin4jUtils;

/**
 * 负责与区域设置的内容
 * @author 15443
 *
 */
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class areaAction extends CommonAction<Area>{
	private static final long serialVersionUID = 1L;
	
	//使用属性驱动获取用户上传的文件
	private File file;
	public void setFile(File file) {
		this.file = file;
	}
	
	@Autowired
	private AreaService areaService;
	
	@Action(value = "areaAction_importXSL",results = {@Result(name="success",
			location = "/pages/base/area.html", type="redirect")})
    public String importXSL() {
		try {
			//用于存放Area的集合
			List<Area> list = new ArrayList<Area>();
			//加载文件
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
			//读取第一个工作簿的内容(xml文件有很多工作簿)
			HSSFSheet sheet = workbook.getSheetAt(0);
			//遍历所有行
			for (Row row : sheet) {
				//跳过所有行
				if(row.getRowNum() == 0) {
					continue;
				}
				//获取每行对应列中的数据,getCell()中的参数是表示第几列
				String province = row.getCell(1).getStringCellValue();
				String city = row.getCell(2).getStringCellValue();
				String district = row.getCell(3).getStringCellValue();
				String postcode = row.getCell(4).getStringCellValue();
				
				//截掉省市区的最后一个字符
				province = province.substring(0, province.length() - 1);
				city = city.substring(0, city.length() - 1);
				district = district.substring(0, district.length() - 1);
				//生成城市简码,第二个参数表示替代拼音之间空格的字符
				String citycode = PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
				//生成简码
				String[] headByString = PinYin4jUtils.getHeadByString(province + city + district);
				String shortcode = PinYin4jUtils.stringArrayToString(headByString);
				
				//手动封装area
				Area area = new Area();
				area.setProvince(province);
				area.setCity(city);
				area.setDistrict(district);
				area.setPostcode(postcode);
				area.setCitycode(citycode);
				area.setShortcode(shortcode);
				
				list.add(area);
			}
			areaService.save(list);
			//释放资源
			workbook.close();
		} catch (Exception e) {
		}
		return SUCCESS;
	}	
	/**
	 * 分页查询区域
	 * @return
	 */
	// 分页查询,EasyUI请求的方式是AJAX,返回的数据应该是JSON格式的
    @Action("areaAction_pageQuery")
    public String pageQuery() throws IOException {
        // EasyUI的页码从1开始,SpringDataJPA框架构造PageRequest的时候,page是从0开始的
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Area> page = areaService.pageQuery(pageable);
       
        page2Json(page, new String[] {"subareas"});

        return NONE;
    }
    /**
     * 查询所有区域信息
     * @return
     * @throws IOException
     */
    //使用属性驱动动态获取用户输入的内容
    private String q;
    public void setQ(String q) {
		this.q = q;
	}
    @Action("areaAction_findAll")
    public String findAll() throws IOException {
    	List<Area> list = null;
    	
    	if(StringUtils.isEmpty(q)) {
    		list = areaService.findAll();
    	}else {
    		list = areaService.findByQ(q);
    	}
    	//调用list2json方法,将list转换成json后响应给前端
    	list2Json(list,new String[] {"subareas"});
        return NONE;
    }
    
	/**
	 * 导出为Excel文件
	 * @return
	 * @throws IOException
	 */
	
	@Action(value = "areaAction_exportExcel")
	public String exportExcel() throws IOException {
		//先获取所有的区域信息
		List<Area> list = areaService.findAll();
		
		//创建Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		//创建并初始化工作簿
		HSSFSheet sheet = workbook.createSheet("区域数据统计");
		
		//标题行
		HSSFRow titleRow = sheet.createRow(0);
		titleRow.createCell(0).setCellValue("编号");
		titleRow.createCell(1).setCellValue("省");
		titleRow.createCell(2).setCellValue("市");
		titleRow.createCell(3).setCellValue("区");
		titleRow.createCell(4).setCellValue("邮编");
		titleRow.createCell(5).setCellValue("简码");
		titleRow.createCell(6).setCellValue("城市编码");
		
		for (Area area : list) {
		 int currentRowNum = sheet.getLastRowNum() + 1;
		 
		 //数据行
		 HSSFRow dataRow = sheet.createRow(currentRowNum);
		 dataRow.createCell(0).setCellValue(currentRowNum);
		 dataRow.createCell(1).setCellValue(area.getProvince());
		 dataRow.createCell(2).setCellValue(area.getCity());
         dataRow.createCell(3).setCellValue(area.getDistrict());
         dataRow.createCell(4).setCellValue(area.getPostcode());
         dataRow.createCell(5).setCellValue(area.getShortcode());
         dataRow.createCell(6).setCellValue(area.getCitycode());
		}
		
		//一个流两个头
		String fileName = "区域数据.xls";
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		
		ServletContext servletContext = ServletActionContext.getServletContext();
		ServletOutputStream os = response.getOutputStream();
		
		//获取客户端浏览器的类型
		String agent = request.getHeader("User-Agent");
		//获取文件的MineType(文件类型),必须写在重新编码的代码之前
		String mimeType = servletContext.getMimeType(fileName);
		
		//对文件名重新编码
		fileName = FileDownloadUtils.encodeDownloadFilename(fileName, agent);
		
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition","attachment;fileName=" + fileName);
		
		//写出文件
		workbook.write(os);
		workbook.close();
		return NONE;
	}
	
    /**
     * @return
     * @throws IOException
     * 获取导出为HighCharts所需要的数据
     */
    @Action("areaAction_getHighChartsData")
    public String getHighChartsData() throws IOException {
    	List<Object[]> list = areaService.getHighChartsData();
    	list2Json(list, null);
    	return NONE;
    }
    
    
    
    
    
}
