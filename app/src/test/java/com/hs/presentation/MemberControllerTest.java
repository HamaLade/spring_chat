package com.hs.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.application.member.service.MemberAuthService;
import com.hs.persistence.repository.memeber.MemberRepository;
import com.hs.presentation.error.Errors;
import com.hs.presentation.auth.dto.MemberLoginRequestDto;
import com.hs.presentation.auth.dto.MemberSignUpRequestDto;
import com.hs.utils.FieldDescriptorUtils;
import com.hs.utils.RequestCookiesSnippet;
import com.hs.utils.ResponseCookiesSnippet;
import com.hs.utils.jwt.MemberJwtProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 컨트롤러 테스트")
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql({"/sql/test/schema.sql", "/sql/test/data.sql"})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberAuthService memberAuthService;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입")
    void signUp() throws Exception {

        String loginId = "signupTest01";
        String nickname = "signupTest01";
        String password = "signupTest01";
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto(loginId, nickname, password);

        mockMvc.perform(post(ApiPaths.MEMBER_SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequestDto))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("members-sign-up",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                ),
                                responseFields(
                                        FieldDescriptorUtils.nullDataReponseMessageFieldDescriptor("반환 데이터 없음")
                                )
                        )
                )
        ;
    }

    @Test
    @DisplayName("회원 가입: 중복된 로그인 아이디")
    void signUpFailedCase01() throws Exception {

        String loginId = "test01";
        String nickname = "test000";
        String password = "test01";
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto(loginId, nickname, password);

        mockMvc.perform(post(ApiPaths.MEMBER_SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequestDto))
                )
                .andExpect(status().is(Errors.SIGNUP_FAILED.getStatus()))
                .andDo(print())
                .andDo(
                        document("members-sign-up-failed-case01",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                ),
                                responseFields(
                                        FieldDescriptorUtils.nullDataReponseMessageFieldDescriptor("반환 데이터 없음")
                                )
                        )
                )
        ;
    }

    @Test
    @DisplayName("회원 가입: 중복된 닉네임")
    void signUpFailedCase02() throws Exception {

        String loginId = "test000";
        String nickname = "test01";
        String password = "test01";
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto(loginId, nickname, password);

        mockMvc.perform(post(ApiPaths.MEMBER_SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequestDto))
                )
                .andExpect(status().is(Errors.SIGNUP_FAILED.getStatus()))
                .andDo(print())
                .andDo(
                        document("members-sign-up-failed-case02",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                ),
                                responseFields(
                                        FieldDescriptorUtils.nullDataReponseMessageFieldDescriptor("반환 데이터 없음")
                                )
                        )
                )
        ;
    }

    @Test
    @DisplayName("회원 로그인")
    void login() throws Exception {

        String loginId = "test01";
        String password = "test01";

        Mockito.when(passwordEncoder.matches(password, password)).thenReturn(true);

        mockMvc.perform(post(ApiPaths.MEMBER_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MemberLoginRequestDto(loginId, password)))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("members-login",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                ),
                                responseFields(
                                        FieldDescriptorUtils.reponseMessageFieldDescriptor(
                                                "로그인 성공 시 반환되는 JWT토큰 정보",
                                                subsectionWithPath("data.accessTokenInfo").type(JsonFieldType.OBJECT).description("액세스 토큰 정보"),
                                                fieldWithPath("data.accessTokenInfo.token").type(JsonFieldType.STRING).description("액세스 토큰"),
                                                fieldWithPath("data.accessTokenInfo.expiredTime").type(JsonFieldType.NUMBER).description("액세스 토큰 만료 시간"),
                                                subsectionWithPath("data.refreshTokenInfo").type(JsonFieldType.OBJECT).description("리프레시 토큰 정보"),
                                                fieldWithPath("data.refreshTokenInfo.token").type(JsonFieldType.STRING).description("리프레시 토큰"),
                                                fieldWithPath("data.refreshTokenInfo.expiredTime").type(JsonFieldType.NUMBER).description("리프레시 토큰 만료 시간")
                                        )
                                ),
                                ResponseCookiesSnippet.customResponseHeaderCookies(
                                        headerWithName(MemberJwtProperties.ACCESS_TOKEN_NAME).description("액세스 토큰"),
                                        headerWithName(MemberJwtProperties.REFRESH_TOKEN_NAME).description("리프레시 토큰")
                                )
                        )
                );
    }

    @Test
    @DisplayName("회원 로그인: 로그인 실패")
    void loginFailedCase01() throws Exception {

        String loginId = "test01";
        String password = "tesT01";

        mockMvc.perform(post(ApiPaths.MEMBER_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MemberLoginRequestDto(loginId, password)))
                )
                .andExpect(status().is(Errors.LOGIN_FAILED.getStatus()))
                .andDo(print())
                .andDo(
                        document("members-login-failed-case01",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                ),
                                responseFields(
                                        FieldDescriptorUtils.nullDataReponseMessageFieldDescriptor("반환 데이터 없음")
                                )
                        )
                );
    }

    @Test
    @DisplayName("회원 로그아웃")
    void logout() throws Exception {

        mockMvc.perform(post(ApiPaths.MEMBER_LOGOUT))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("members-logout",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseBody(),
                                RequestCookiesSnippet.customRequestHeaderCookies(
                                        headerWithName(MemberJwtProperties.ACCESS_TOKEN_NAME).optional().description("액세스 토큰 (Optional)"),
                                        headerWithName(MemberJwtProperties.REFRESH_TOKEN_NAME).optional().description("리프레시 토큰 (Optional)")
                                ),
                                ResponseCookiesSnippet.customResponseHeaderCookies(
                                        headerWithName(MemberJwtProperties.ACCESS_TOKEN_NAME).description("액세스 토큰 (값 비움, 만료시간 0)"),
                                        headerWithName(MemberJwtProperties.REFRESH_TOKEN_NAME).description("리프레시 토큰 (값 비움, 만료시간 0)")
                                )
                        )
                );
    }



}
