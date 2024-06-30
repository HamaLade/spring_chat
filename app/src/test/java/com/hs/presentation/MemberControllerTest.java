package com.hs.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.application.member.service.MemberAuthService;
import com.hs.persistence.repository.memeber.MemberRepository;
import com.hs.presentation.error.Errors;
import com.hs.presentation.member.dto.MemberSignUpRequestDto;
import com.hs.utils.FieldDescriptorUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
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

        mockMvc.perform(post("/members/signup")
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
    void signUpFailed() throws Exception {

        String loginId = "test01";
        String nickname = "test01";
        String password = "test01";
        MemberSignUpRequestDto memberSignUpRequestDto = new MemberSignUpRequestDto(loginId, nickname, password);

        mockMvc.perform(post("/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequestDto))
                )
                .andExpect(status().is(Errors.SIGNUP_FAILED.getStatus()))
                .andDo(print())
                .andDo(
                        document("members-sign-up-failed",
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

}
