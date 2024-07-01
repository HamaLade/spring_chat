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
        fieldDescriptors.add(fieldWithPath("message").description("응답 메시지"));
        fieldDescriptors.add(subsectionWithPath("data").optional().description(dataDescription));
        fieldDescriptors.addAll(Arrays.asList(data));
        responseMessageFieldErrorDescriptors(fieldDescriptors);

        return fieldDescriptors.toArray(new FieldDescriptor[0]);
    }

    public static FieldDescriptor[] nullDataReponseMessageFieldDescriptor(String dataDescription) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        fieldDescriptors.add(fieldWithPath("message").description("응답 메시지"));
        fieldDescriptors.add(subsectionWithPath("data").optional().description(dataDescription));
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
