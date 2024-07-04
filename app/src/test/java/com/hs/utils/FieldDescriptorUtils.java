package com.hs.utils;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

public class FieldDescriptorUtils {

    public static FieldDescriptor[] reponseMessageFieldDescriptor(String dataDescription, FieldDescriptor... data) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        fieldDescriptors.add(fieldWithPath("message").optional().type(JsonFieldType.STRING).description("응답 메시지"));
        fieldDescriptors.add(subsectionWithPath("data").optional().description(dataDescription));
        fieldDescriptors.addAll(Arrays.asList(data));
        responseMessageFieldErrorDescriptors(fieldDescriptors);

        return fieldDescriptors.toArray(new FieldDescriptor[0]);
    }

    public static FieldDescriptor[] nullDataReponseMessageFieldDescriptor(String dataDescription) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        fieldDescriptors.add(fieldWithPath("message").optional().type(JsonFieldType.STRING).description("응답 메시지"));
        fieldDescriptors.add(subsectionWithPath("data").optional().type(JsonFieldType.OBJECT).description(dataDescription));
        responseMessageFieldErrorDescriptors(fieldDescriptors);

        return fieldDescriptors.toArray(new FieldDescriptor[0]);
    }

    public static FieldDescriptor[] responseMessagePageFieldDescriptor(String dataDescription, FieldDescriptor... data) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        fieldDescriptors.add(fieldWithPath("message").optional().type(JsonFieldType.STRING).description("응답 메시지"));
        fieldDescriptors.add(subsectionWithPath("data").type(JsonFieldType.OBJECT).description(dataDescription));
        fieldDescriptors.add(subsectionWithPath("data.pageable").type(JsonFieldType.OBJECT).description(dataDescription));
        fieldDescriptors.add(fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"));
        fieldDescriptors.add(fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"));
        fieldDescriptors.add(fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"));
        fieldDescriptors.add(fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 여부"));
        fieldDescriptors.add(fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징 되지 않음 여부"));
        fieldDescriptors.add(fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"));
        fieldDescriptors.add(fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"));
        fieldDescriptors.add(fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수"));
        fieldDescriptors.add(fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"));
        fieldDescriptors.add(fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("페이지 번호"));
        fieldDescriptors.add(subsectionWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬"));
        fieldDescriptors.add(fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("비어있는지 여부"));
        fieldDescriptors.add(fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"));
        fieldDescriptors.add(fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 되지 않음 여부"));
        fieldDescriptors.add(fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"));
        fieldDescriptors.add(fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("요소 수"));
        fieldDescriptors.add(fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("비어있는지 여부"));
        fieldDescriptors.add(subsectionWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("페이지 정렬"));
        fieldDescriptors.add(fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("비어있는지 여부"));
        fieldDescriptors.add(fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"));
        fieldDescriptors.add(fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 되지 않음 여부"));
        fieldDescriptors.addAll(Arrays.asList(data));
        responseMessageFieldErrorDescriptors(fieldDescriptors);

        return fieldDescriptors.toArray(new FieldDescriptor[0]);
    }

    private static void responseMessageFieldErrorDescriptors(List<FieldDescriptor> fieldDescriptors) {
        fieldDescriptors.add(subsectionWithPath("error").optional().type(JsonFieldType.OBJECT).description("에러 정보"));
        fieldDescriptors.add(fieldWithPath("error.message").optional().type(JsonFieldType.STRING).description("에러 메시지"));
        fieldDescriptors.add(fieldWithPath("error.code").optional().type(JsonFieldType.NUMBER).description("에러 코드"));
        fieldDescriptors.add(fieldWithPath("error.status").optional().type(JsonFieldType.NUMBER).description("HTTP 상태 코드"));
        fieldDescriptors.add(fieldWithPath("error.description").optional().type(JsonFieldType.STRING).description("에러 설명"));
    }



}
