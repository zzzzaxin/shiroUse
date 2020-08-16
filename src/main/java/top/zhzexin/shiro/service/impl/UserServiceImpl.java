package top.zhzexin.shiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhzexin.shiro.dao.RoleMapper;
import top.zhzexin.shiro.dao.UserMapper;
import top.zhzexin.shiro.domain.Role;
import top.zhzexin.shiro.domain.User;
import top.zhzexin.shiro.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findAllUserInfoByUsername(String username) {
        User user = userMapper.findByUsername(username);

        //用户的角色集合
        List<Role> roleList =  roleMapper.findRoleListByUserId(user.getId());


        user.setRoleList(roleList);

        return user;
    }


    @Override
    public User findSimpleUserInfoById(int userId) {
        return userMapper.findById(userId);
    }


    @Override
    public User findSimpleUserInfoByUsername(String username) {
        return userMapper.findByUsername(username);
    }


}
