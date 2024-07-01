package com.hs.utils;

import org.springframework.restdocs.headers.AbstractHeadersSnippet;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.RequestCookie;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;

public class RequestCookiesSnippet extends AbstractHeadersSnippet {
    /**
     * Creates a new {@code AbstractHeadersSnippet} that will produce a snippet named
     * {@code <type>-headers}. The headers will be documented using the given
     * {@code  descriptors} and the given {@code attributes} will be included in the model
     * during template rendering.
     *
     * @param descriptors the header descriptors
     * @param attributes  the additional attributes
     */
    protected RequestCookiesSnippet(List<HeaderDescriptor> descriptors, Map<String, Object> attributes) {
        super("cookie-request", descriptors, attributes); // type : snippet name 정의
    }

    @Override
    protected Set<String> extractActualHeaders(Operation operation) {
        return operation.getRequest().getCookies().stream()
                .map(RequestCookie::getName)
                .collect(java.util.stream.Collectors.toSet())
                ;
    }

    public static RequestCookiesSnippet customRequestHeaderCookies(
            HeaderDescriptor... descriptors) {
        return new RequestCookiesSnippet(Arrays.asList(descriptors), null);
    }

    public static HeaderDescriptor cookieWithName(String cookieName) {
        return headerWithName(cookieName);
    }

}
