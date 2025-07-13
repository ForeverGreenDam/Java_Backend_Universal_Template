package com.greendam.template.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.greendam.template.common.context.BaseContext;
import com.greendam.template.common.utils.JwtUtil;
import com.greendam.template.common.utils.ThrowUtils;
import com.greendam.template.constant.ErrorConstant;
import com.greendam.template.constant.JwtClaimsConstant;
import com.greendam.template.exception.ErrorCode;
import com.greendam.template.mapper.UserMapper;
import com.greendam.template.model.dto.UserLoginDTO;
import com.greendam.template.model.entity.User;
import com.greendam.template.model.vo.UserLoginVO;
import com.greendam.template.model.vo.UserVO;
import com.greendam.template.properties.JwtProperties;
import com.greendam.template.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 * @author ForeverGreenDam
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    JwtProperties jwtProperties;
    @Override
    public UserLoginVO loginByJwt(UserLoginDTO userLoginDTO) {
        String userAccount = userLoginDTO.getUserAccount();
        String userPassword = DigestUtils.md5DigestAsHex(userLoginDTO.getUserPassword().getBytes());
       //不用验证码的情况
        User queryUser = new User();
        queryUser.setUserAccount(userAccount);
        User user= userMapper.select(queryUser);
        ThrowUtils.throwIf(user==null, ErrorCode.NOT_FOUND_ERROR, ErrorConstant.LOGIN_ERROR);
        //验证密码
        ThrowUtils.throwIf(!user.getUserPassword().equals(userPassword), ErrorCode.NOT_FOUND_ERROR, ErrorConstant.LOGIN_ERROR);
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO userLoginVO = BeanUtil.copyProperties(user, UserLoginVO.class);
        userLoginVO.setToken(token);
        return userLoginVO;
    }

    @Override
    public UserVO getUser() {
        // 基于ThreadLocal获取当前登录用户信息
        Long currentId = BaseContext.getCurrentId();
        ThrowUtils.throwIf(currentId == null, ErrorCode.NOT_FOUND_ERROR, ErrorConstant.USER_NOT_LOGIN);
        User tempUser= new User();
        tempUser.setId(currentId);
        User user = userMapper.select(tempUser);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, ErrorConstant.USER_NOT_FOUND);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @Override
    public UserVO getUser(HttpServletRequest request) {
        return null;
    }
}
