package com.fsck.k9.message.html;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;

import java.util.List;

import static java.util.Arrays.asList;
class HeadCleaner {
    private static final List<String> ALLOWED_TAGS = asList("style", "meta");


    public void clean(Document dirtyDocument, Document cleanedDocument) {
        copySafeNodes(dirtyDocument.head(), cleanedDocument.head());
    }

    private void copySafeNodes(Element source, Element destination) {
        CleaningVisitor cleaningVisitor = new CleaningVisitor(source, destination);
        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(source);
    }


    
}