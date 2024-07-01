package com.hs.utils;

import org.springframework.restdocs.headers.AbstractHeadersSnippet;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.ResponseCookie;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;

public class ResponseCookiesSnippet extends AbstractHeadersSnippet {
    /**
     * Creates a new {@code AbstractHeadersSnippet} that will produce a snippet named
     * {@code <type>-headers}. The headers will be documented using the given
     * {@code  descriptors} and the given {@code attributes} will be included in the model
     * during template rendering.
     *
     * @param descriptors the header descriptors
     * @param attributes  the additional attributes
     */
    protected ResponseCookiesSnippet(List<HeaderDescriptor> descriptors, Map<String, Object> attributes) {
        super("cookie-response", descriptors, attributes); // type : snippet name 정의
    }

    @Override    protected Set<String> extractActualHeaders(Operation operation) {
        return operation.getResponse().getCookies().stream().map(ResponseCookie::getName).collect(Collectors.toSet());
    }

    public static ResponseCookiesSnippet customResponseHeaderCookies(
            HeaderDescriptor... descriptors) {
        return new ResponseCookiesSnippet(Arrays.asList(descriptors), null);
    }

    public static HeaderDescriptor cookieWithName(String cookieName) {
        return headerWithName(cookieName);
    }

}
