package com.greendam.template.controller;

import com.greendam.template.common.BaseResponse;
import com.greendam.template.common.context.BaseContext;
import com.greendam.template.exception.ErrorCode;
import com.greendam.template.model.dto.UserLoginDTO;
import com.greendam.template.model.dto.UserRegisterDTO;
import com.greendam.template.model.dto.UserUpdateDTO;
import com.greendam.template.model.vo.UserLoginVO;
import com.greendam.template.model.vo.UserVO;
import com.greendam.template.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制器类
 * @author ForeverGreenDam
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     * @param userRegisterDTO 用户注册数据传输对象
     * @return 用户ID响应
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        Long id = userService.register(userRegisterDTO);
        return BaseResponse.success(id);
    }

    /**
     * 用户登录接口
     * @param userLoginDTO 用户登录数据传输对象
     * @return 用户信息响应
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        //使用JWT进行用户登录
        UserLoginVO userLoginVO = userService.loginByJwt(userLoginDTO);
        return BaseResponse.success(userLoginVO);
    }
    /**
     * 用户登出接口
     * @return 登出成功响应
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout() {
        // 清理当前线程上下文中的用户信息（JWT 为无状态，可同时在客户端删除 token）
        BaseContext.removeCurrentId();
        return BaseResponse.success(true);
    }
    /**
     * 删除用户/注销用户接口
     * @return 删除/注销成功响应
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser() {
        boolean ok = userService.deleteUser(null);
        return BaseResponse.success(ok);
    }
    /**
     * 更新用户信息接口（涉及敏感操作，仅限管理员使用）
     * @param userUpdateDTO 用户更新数据传输对象
     * @return 更新成功响应
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO == null || userUpdateDTO.getId() == null) {
            return BaseResponse.error(ErrorCode.PARAMS_ERROR);
        }
        // 权限校验：只有管理员可更新敏感信息
        UserVO current = userService.getUser();
        if (current == null || !com.greendam.template.constant.UserRoleConstant.ADMIN.equals(current.getUserRole())) {
            return BaseResponse.error(com.greendam.template.exception.ErrorCode.NOT_AUTH_ERROR);
        }
        boolean ok = userService.updateUser(userUpdateDTO);
        return BaseResponse.success(ok);
    }

    /**
     * 编辑用户信息接口（不允许编辑敏感操作）
     * @param userUpdateDTO 用户更新数据传输对象
     * @return 编辑成功响应
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO == null) {
            return BaseResponse.error(ErrorCode.PARAMS_ERROR);
        }
        boolean ok = userService.editUser(userUpdateDTO);
        return BaseResponse.success(ok);
    }

    /**
     * 获取用户信息接口
     * @param id 用户ID
     * @return 用户信息响应
     */
    @GetMapping("/getInfo")
    public BaseResponse<UserVO> getUser(Long id) {
        UserVO user = userService.getUser();
        return BaseResponse.success(user);
    }
}
