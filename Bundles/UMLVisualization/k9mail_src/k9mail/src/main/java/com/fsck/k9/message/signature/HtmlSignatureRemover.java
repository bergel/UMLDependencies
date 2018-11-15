package com.fsck.k9.message.signature;


import com.fsck.k9.helper.jsoup.AdvancedNodeTraversor;
import com.fsck.k9.message.html.HtmlProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class HtmlSignatureRemover {
    public static String stripSignature(String content) {
        return new HtmlSignatureRemover().stripSignatureInternal(content);
    }

    private String stripSignatureInternal(String content) {
        Document document = Jsoup.parse(content);

        AdvancedNodeTraversor nodeTraversor = new AdvancedNodeTraversor(new StripSignatureFilter());
        nodeTraversor.filter(document.body());

        return HtmlProcessor.toCompactString(document);
    }


    
}