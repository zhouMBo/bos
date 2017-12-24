package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.SubArea;

public interface SubAreaService {

	void save(SubArea subArea);

	Page<SubArea> pageQuery(Pageable pageable);


}
