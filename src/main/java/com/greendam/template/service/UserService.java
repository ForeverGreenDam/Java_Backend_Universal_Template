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
}
