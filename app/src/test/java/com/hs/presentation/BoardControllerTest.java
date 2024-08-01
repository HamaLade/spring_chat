package com.hs.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.application.board.service.BoardService;
import com.hs.persistence.entity.board.BoardPost;
import com.hs.persistence.repository.board.BoardPostRepository;
import com.hs.presentation.board.dto.NoticePostWriteRequestDto;
import com.hs.utils.FieldDescriptorUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("게시판 컨트롤러 테스트")
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql({"/sql/test/schema.sql", "/sql/test/data.sql"})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardService boardService;

    @SpyBean
    private BoardPostRepository boardPostRepository;

    @Test
    @DisplayName("공지사항 목록 (메인페이지)")
    void noticeDefaultPageList() throws Exception {
        mockMvc.perform(
                post(ApiPaths.GET_NOTICE_BOARD)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("default-notice-board",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                FieldDescriptorUtils.responseMessagePageFieldDescriptor(
                                        "공지사항 목록 페이지 정보",
                                        fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                        fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("data.content[].textContent").type(JsonFieldType.STRING).description("게시글 내용")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("공지사항 목록 조회 페이지")
    void noticePageList() throws Exception {
        mockMvc.perform(
                get(ApiPaths.GET_NOTICE_BOARD_PAGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "10")
        ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML + ";charset=UTF-8"))
                .andExpect(view().name("notice-page"))
                .andDo(document("notice-board-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseHeaders(
                                headerWithName("Content-Type").description("The Content-Type of the response")
                        )
                ));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("공지사항 작성")
    void writeNoticePost() throws Exception {
        NoticePostWriteRequestDto noticePostWriteRequestDto = new NoticePostWriteRequestDto(
                "제목",
                "내용"
        );

        mockMvc.perform(
                post(ApiPaths.WRITE_NOTICE_POST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticePostWriteRequestDto))
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("write-notice-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("textContent").type(JsonFieldType.STRING).description("게시글 내용")
                        ),
                        responseFields(
                                FieldDescriptorUtils.reponseMessageFieldDescriptor("공지사항 작성 결과")
                        )
                ));
    }

    @Test
    @DisplayName("공지사항 작성: 권한없음")
    void writeNoticePostNoPermission() throws Exception {
        NoticePostWriteRequestDto noticePostWriteRequestDto = new NoticePostWriteRequestDto(
                "제목",
                "내용"
        );

        mockMvc.perform(
                        post(ApiPaths.WRITE_NOTICE_POST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(noticePostWriteRequestDto))
                )
                .andExpect(status().isForbidden())
                .andDo(print())
                .andDo(document("write-notice-post-no-permission",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("textContent").type(JsonFieldType.STRING).description("게시글 내용")
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("공지사항 수정")
    void editNoticePost() throws Exception {

        String title = "notice title 01";
        BoardPost boardPost = boardPostRepository.findByTitle(title).orElseThrow();
        NoticePostWriteRequestDto noticePostWriteRequestDto = new NoticePostWriteRequestDto(
                title,
                "내용 변경"
        );

        mockMvc.perform(
                post(ApiPaths.EDIT_NOTICE_POST, boardPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticePostWriteRequestDto))
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("edit-notice-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("textContent").type(JsonFieldType.STRING).description("게시글 내용")
                        ),
                        responseFields(
                                FieldDescriptorUtils.reponseMessageFieldDescriptor("공지사항 수정 결과")
                        )
                ));
    }

}
