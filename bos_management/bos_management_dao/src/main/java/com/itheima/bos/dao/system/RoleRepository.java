package com.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	//  on ur.C_USER_ID = u.C_ID WHERE u.C_ID = 203;
    //  from Role r inner join r.users u where u.id = ? == select * from Role r inner join r.users u where u.id = ?
    // 上面这个语句查询的时候,会把所有的关联表的数据都查询出来, 查询了Role / User  / Role_User三张表,
    // 查询完成以后,会把得到的结果封装到Role/ User这两个JavaBean身上,再把这两个JavaBean装在一个数组中
   @Query("select r from Role r inner join r.users u where u.id = ?")
	List<Role> findByUid(Long uid);

}
