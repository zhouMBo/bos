package com.itheima.bos.service.base.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierRepository courierRepository;
	
	/**
	 * 新建邮递员
	 */
	@Override
	public void save(Courier courier) {
		courierRepository.save(courier);
	}

	/**
	 * 分页查询
	 */
	@Override
	public Page<Courier> pageQuery(Specification<Courier> specification, Pageable pageable) {
		return courierRepository.findAll(specification,pageable);
	}
	/**
	 * 删除快递员
	 * @RequiresPermissions:其后面的值可以任意设置,只需要在realm的AuthorizationInfo方法中进行授权时相对应
	 */
	@RequiresPermissions("courier:delete")
	@Override
	public void batchDel(String ids) {
		if (StringUtils.isNotEmpty(ids)) {
			String[] split = ids.split(",");
			for (String id : split) {
				courierRepository.updateDelTag(Long.parseLong(id));
			}
		}
	}

	/**
	 * 查询所有在职快递员
	 */
	@Override
	public List<Courier> findAll() {
		return courierRepository.findByDeltagIsNull();
	}

}
