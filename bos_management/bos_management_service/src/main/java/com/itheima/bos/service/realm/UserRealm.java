package com.itheima.bos.service.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.system.PermissionRepository;
import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.dao.system.UserRepository;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;

import antlr.Token;

@Component
public class UserRealm extends AuthorizingRealm{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	
	/**
	 * 进行授权操作
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalcollection) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//对相应的请求进行授权
/*	
 * 		info.addStringPermission("courier_pageQuery");
		info.addStringPermission("courier:delete");
		info.addRole("admin");*/
		
		//获取当前用户
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		// 从数据库中查询当前用户对应的权限和角色
		
		
		// 内置管理员授予所有的角色和权限
		if ("admin".equals(user.getUsername())) {
			//获取所有的角色 , 管理员拥有所有的角色
			List<Role> roles = roleRepository.findAll();
			for (Role role : roles) {
				info.addRole(role.getKeyword());
			}
			//获取所有的权限 , 管理员拥有所有的权限
			List<Permission> permissions = permissionRepository.findAll();
			for (Permission permission : permissions) {
				info.addStringPermission(permission.getKeyword());
			}
		}else {
			//根据user的id查出对应的所有角色
			List<Role> roles = roleRepository.findByUid(user.getId());
			for (Role role : roles) {
				info.addRole(role.getKeyword());
			}
			//根据user的id查出对应的所有权限
			List<Permission> permissions = permissionRepository.findByUid(user.getId());
			for (Permission permission : permissions) {
				info.addStringPermission(permission.getKeyword());
			}
		}
		return info;
	}

	/**
	 * 进行认证操作
	 * 参数中的token其实就是subject.login(token)方法传递过来的参数
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {
		// 首先根据用户名进行查找
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		
		String username = usernamePasswordToken.getUsername();
		User user = userRepository.findByUsername(username);
		
		// 如果没有找到用户,抛出异常
		if (user == null) {
			return null;
		}
		
		// 如果找到了用户,进行用户名和密码的比对,比对的操作由框架来完成的
	   /**
        * @param principal the 'primary' principal associated with the specified realm.
        *        当事人.一般传递的参数就是数据库中查询到的用户.这个参数决定了subject.getPrincipal()的返回值
        * @param credentials the credentials that verify the given principal. 凭证.密码.是从数据库中查出来的密码
        * @param realmName the realm from where the principal and credentials were acquired.
        *        当前realm的名字
        */
		AuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),getName());

		return info;
	}

}
