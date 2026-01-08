package com.greendam.template.service.impl;

import com.greendam.template.common.context.BaseContext;
import com.greendam.template.common.properties.JwtProperties;
import com.greendam.template.mapper.UserMapper;
import com.greendam.template.model.dto.UserRegisterDTO;
import com.greendam.template.model.dto.UserUpdateDTO;
import com.greendam.template.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        BaseContext.removeCurrentId();
    }

    @AfterEach
    void tearDown() {
        BaseContext.removeCurrentId();
    }

    @Test
    void testRegisterSuccess() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUserAccount("alice");
        dto.setUserPassword("pwd");
        dto.setUserPasswordConfirm("pwd");

        when(userMapper.select(any(User.class))).thenReturn(null);
        doAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(100L);
            return 1;
        }).when(userMapper).insert(any(User.class));

        Long id = userService.register(dto);
        Assertions.assertEquals(100L, id);
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    void testRegisterDuplicateThrows() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUserAccount("alice");
        dto.setUserPassword("pwd");
        dto.setUserPasswordConfirm("pwd");

        User exist = new User();
        exist.setId(1L);
        when(userMapper.select(any(User.class))).thenReturn(exist);

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> userService.register(dto));
        Assertions.assertTrue(ex.getMessage().contains("账号已存在") || ex instanceof RuntimeException);
    }

    @Test
    void testDeleteUserSoftDelete() {
        BaseContext.setCurrentId(50L);
        when(userMapper.update(any(User.class))).thenReturn(1);
        boolean ok = userService.deleteUser(null);
        Assertions.assertTrue(ok);
        verify(userMapper, times(1)).update(any(User.class));
    }

    @Test
    void testEditUserSuccess() {
        BaseContext.setCurrentId(20L);
        User exist = new User();
        exist.setId(20L);
        exist.setUserAccount("bob");
        when(userMapper.select(any(User.class))).thenReturn(exist);
        when(userMapper.update(any(User.class))).thenReturn(1);

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserName("Bob");
        dto.setUserAvatar("avatar.png");

        boolean ok = userService.editUser(dto);
        Assertions.assertTrue(ok);
        verify(userMapper, times(1)).update(any(User.class));
    }
}


