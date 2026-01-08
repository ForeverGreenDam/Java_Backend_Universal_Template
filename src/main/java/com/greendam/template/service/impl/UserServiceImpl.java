package com.greendam.template.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.greendam.template.common.context.BaseContext;
import com.greendam.template.common.properties.JwtProperties;
import com.greendam.template.common.utils.JwtUtil;
import com.greendam.template.common.utils.PasswordUtils;
import com.greendam.template.common.utils.ThrowUtils;
import com.greendam.template.constant.ErrorConstant;
import com.greendam.template.constant.JwtClaimsConstant;
import com.greendam.template.constant.UserRoleConstant;
import com.greendam.template.exception.ErrorCode;
import com.greendam.template.mapper.UserMapper;
import com.greendam.template.model.dto.UserLoginDTO;
import com.greendam.template.model.dto.UserRegisterDTO;
import com.greendam.template.model.dto.UserUpdateDTO;
import com.greendam.template.model.entity.User;
import com.greendam.template.model.vo.UserLoginVO;
import com.greendam.template.model.vo.UserVO;
import com.greendam.template.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
       // 不用验证码的情况
        User queryUser = new User();
        queryUser.setUserAccount(userAccount);
        User user= userMapper.select(queryUser);
        ThrowUtils.throwIf(user==null, ErrorCode.NOT_FOUND_ERROR, ErrorConstant.LOGIN_ERROR);
        // 验证密码（bcrypt）
        ThrowUtils.throwIf(!PasswordUtils.matches(userLoginDTO.getUserPassword(), user.getUserPassword()),
                ErrorCode.NOT_FOUND_ERROR, ErrorConstant.LOGIN_ERROR);
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
        // 基于 session 获取用户信息的简单实现：读取 Header 中的用户 id（如果项目使用 session，请改为从 session 中读取）
        Long currentId = BaseContext.getCurrentId();
        if (currentId == null) {
            return null;
        }
        User tempUser = new User();
        tempUser.setId(currentId);
        User user = userMapper.select(tempUser);
        if (user == null) {
            return null;
        }
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @Override
    public Long register(UserRegisterDTO userRegisterDTO) {
        ThrowUtils.throwIf(userRegisterDTO == null, ErrorCode.PARAMS_ERROR, "请求参数为空");
        String account = userRegisterDTO.getUserAccount();
        String password = userRegisterDTO.getUserPassword();
        String passwordConfirm = userRegisterDTO.getUserPasswordConfirm();
        ThrowUtils.throwIf(account == null || account.isEmpty() || password == null || password.isEmpty()
                || passwordConfirm == null || passwordConfirm.isEmpty(), ErrorCode.PARAMS_ERROR, "参数缺失");
        ThrowUtils.throwIf(!password.equals(passwordConfirm), ErrorCode.PARAMS_ERROR, "两次密码输入不一致");

        // 检查账号是否存在
        User query = new User();
        query.setUserAccount(account);
        User exist = userMapper.select(query);
        ThrowUtils.throwIf(exist != null, ErrorCode.OPERATION_ERROR, "账号已存在");

        // 创建用户
        User toInsert = new User();
        toInsert.setUserAccount(account);
        toInsert.setUserPassword(PasswordUtils.hashPassword(password));
        toInsert.setUserRole(UserRoleConstant.USER);
        toInsert.setIsDelete(0);
        // 插入，mapper 应该填充自增 id
        userMapper.insert(toInsert);
        return toInsert.getId();
    }

    @Override
    public boolean deleteUser(Long id) {
        Long targetId = id;
        if (targetId == null) {
            targetId = BaseContext.getCurrentId();
        }
        ThrowUtils.throwIf(targetId == null, ErrorCode.NOT_LOGIN_ERROR, ErrorConstant.USER_NOT_LOGIN);
        // 软删除：设置 isDelete = 1
        User updateUser = new User();
        updateUser.setId(targetId);
        updateUser.setIsDelete(1);
        int rows = userMapper.update(updateUser);
        return rows > 0;
    }

    @Override
    public boolean updateUser(UserUpdateDTO userUpdateDTO) {
        ThrowUtils.throwIf(userUpdateDTO == null || userUpdateDTO.getId() == null, ErrorCode.PARAMS_ERROR, "参数错误");
        User temp = new User();
        temp.setId(userUpdateDTO.getId());
        User exist = userMapper.select(temp);
        ThrowUtils.throwIf(exist == null, ErrorCode.NOT_FOUND_ERROR, ErrorConstant.USER_NOT_FOUND);

        // 应当校验当前用户是否为管理员，这里只保留位置，实际权限检查在 controller 层或切面中完成
        User toUpdate = new User();
        toUpdate.setId(userUpdateDTO.getId());
        if (userUpdateDTO.getUserPassword() != null && !userUpdateDTO.getUserPassword().isEmpty()) {
            toUpdate.setUserPassword(PasswordUtils.hashPassword(userUpdateDTO.getUserPassword()));
        }
        toUpdate.setUserName(userUpdateDTO.getUserName());
        toUpdate.setUserAvatar(userUpdateDTO.getUserAvatar());
        toUpdate.setUserProfile(userUpdateDTO.getUserProfile());
        toUpdate.setUserRole(userUpdateDTO.getUserRole());
        toUpdate.setIsDelete(userUpdateDTO.getIsDelete());
        int rows = userMapper.update(toUpdate);
        return rows > 0;
    }

    @Override
    public boolean editUser(UserUpdateDTO userUpdateDTO) {
        ThrowUtils.throwIf(userUpdateDTO == null, ErrorCode.PARAMS_ERROR, "参数错误");
        Long currentId = BaseContext.getCurrentId();
        ThrowUtils.throwIf(currentId == null, ErrorCode.NOT_LOGIN_ERROR, ErrorConstant.USER_NOT_LOGIN);

        User temp = new User();
        temp.setId(currentId);
        User exist = userMapper.select(temp);
        ThrowUtils.throwIf(exist == null, ErrorCode.NOT_FOUND_ERROR, ErrorConstant.USER_NOT_FOUND);

        // 仅允许修改非敏感字段：昵称、头像、简介、密码（密码允许）
        User toUpdate = new User();
        toUpdate.setId(currentId);
        if (userUpdateDTO.getUserPassword() != null && !userUpdateDTO.getUserPassword().isEmpty()) {
            toUpdate.setUserPassword(PasswordUtils.hashPassword(userUpdateDTO.getUserPassword()));
        }
        toUpdate.setUserName(userUpdateDTO.getUserName());
        toUpdate.setUserAvatar(userUpdateDTO.getUserAvatar());
        toUpdate.setUserProfile(userUpdateDTO.getUserProfile());
        int rows = userMapper.update(toUpdate);
        return rows > 0;
    }
}
