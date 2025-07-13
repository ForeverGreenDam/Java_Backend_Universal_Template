package com.greendam.template.controller;
import com.greendam.template.common.BaseResponse;
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

        return null;
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

        return BaseResponse.success(true);
    }
    /**
     * 删除用户/注销用户接口
     * @return 删除/注销成功响应
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser() {

        return BaseResponse.success(true);
    }
    /**
     * 更新用户信息接口（涉及敏感操作，仅限管理员使用）
     * @param userUpdateDTO 用户更新数据传输对象
     * @return 更新成功响应
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {

        return BaseResponse.success(true);
    }

    /**
     * 编辑用户信息接口（不允许编辑敏感操作）
     * @param userUpdateDTO 用户更新数据传输对象
     * @return 编辑成功响应
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editUser(@RequestBody UserUpdateDTO userUpdateDTO) {

        return BaseResponse.success(true);
    }

    /**
     * 获取用户信息接口
     * @param id 用户ID
     * @return 用户信息响应
     */
    @GetMapping("/get")
    public BaseResponse<UserVO> getUser(long id) {

        return null;
    }
}
