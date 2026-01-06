package com.greendam.template.service;

import com.greendam.template.model.dto.UserLoginDTO;
import com.greendam.template.model.vo.UserLoginVO;
import com.greendam.template.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务接口
 * @author ForeverGreenDam
 */
public interface UserService {
    /**
     * 使用 JWT 进行用户登录
     * @param userLoginDTO 用户登录数据传输对象
     * @return 用户信息视图对象
     */
     UserLoginVO loginByJwt(UserLoginDTO userLoginDTO);

    /**
     * 获取当前登录用户信息(基于ThreadLocal)
     * @return 用户视图对象
     */
     UserVO getUser();
    /**
     * 获取当前登录用户信息(基于session)
     * @param request HttpServletRequest 对象
     * @return 用户视图对象
     */
    UserVO getUser(HttpServletRequest request);
    /**
     * 注册用户
     * @param userRegisterDTO 注册 DTO
     * @return 新建用户 ID
     */
    Long register(com.greendam.template.model.dto.UserRegisterDTO userRegisterDTO);

    /**
     * 删除（注销）用户（软删）
     * @param id 要删除的用户 ID（为空则删除当前用户）
     * @return 是否成功
     */
    boolean deleteUser(Long id);

    /**
     * 管理员更新用户（敏感字段可改）
     * @param userUpdateDTO 更新 DTO
     * @return 是否成功
     */
    boolean updateUser(com.greendam.template.model.dto.UserUpdateDTO userUpdateDTO);

    /**
     * 用户编辑个人信息（不允许修改敏感字段）
     * @param userUpdateDTO 编辑 DTO
     * @return 是否成功
     */
    boolean editUser(com.greendam.template.model.dto.UserUpdateDTO userUpdateDTO);
}
