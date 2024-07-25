package com.hs.setting.utils.markdown;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkDownUtil {

    /**
     * 마크다운을 HTML로 변환한다.
     *
     * @param markdown 마크다운 데이터
     * @return HTML
     */
    public static String convertMarkDownToHtml(String markdown) {

        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }

}
