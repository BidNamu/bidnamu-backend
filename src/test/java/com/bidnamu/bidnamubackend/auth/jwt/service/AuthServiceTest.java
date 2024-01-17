package com.bidnamu.bidnamubackend.auth.jwt.service;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.bidnamu.bidnamubackend.auth.controller.AuthController;
import com.bidnamu.bidnamubackend.auth.dto.request.LoginRequestDto;
import com.bidnamu.bidnamubackend.auth.dto.response.LoginResponseDto;
import com.bidnamu.bidnamubackend.auth.service.AuthService;
import com.bidnamu.bidnamubackend.user.dto.RegistrationRequestDto;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import com.bidnamu.bidnamubackend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthController authController;
    @Autowired
    private MockMvc mockMvc;

    private final String email = "test1234@gmail.com";
    private final String password = "1234123zXcC!";


    @BeforeEach
    void setting() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        final String nickname = "myNameIs";
        userService.createUser(new RegistrationRequestDto(nickname, email, password));

    }

    @Test
    @Transactional
    @DisplayName("유저가 로그인할 때 액세스토큰과 리프레쉬토큰을 지급받는다.")
    void login() throws Exception {
        String requestBody = "{\"email\":\"test1234@gmail.com\",\"password\":\"1234123zXcC!\"}";
        mockMvc.perform(post("/auths/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists());
    }


    @Test
    @Transactional
    @DisplayName("액세스 토큰을 갱신한다.")
    void reIssue() throws Exception {
        LoginResponseDto responseDto = authService.processLogin(
            new LoginRequestDto(email, password));
        String requestBody =
            "{\"refreshToken\":\"" + responseDto.refreshToken() + "\"}";

        mockMvc.perform(
                post("/auths/reissue")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @Transactional
    @DisplayName("로그아웃 할 때 액세스토큰이 블랙리스트에 등록된다.")
    void logout() throws Exception {
        LoginResponseDto responseDto = authService.processLogin(
            new LoginRequestDto(email, password));
        String requestBody = "{\"accessToken\":\"" + responseDto.accessToken() + "\"}";

        mockMvc.perform(
            delete("/auths/logout").contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.expiration").exists());

    }

}
