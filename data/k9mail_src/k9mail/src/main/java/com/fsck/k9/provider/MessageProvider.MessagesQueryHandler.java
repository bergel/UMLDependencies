package com.fsck.k9.provider;


import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.fsck.k9.activity.MessageInfoHolder;
import com.fsck.k9.controller.MessagingController;
import com.fsck.k9.search.SearchAccount;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
protected class MessagesQueryHandler implements QueryHandler {

        @Override
        public String getPath() {
            return "inbox_messages/";
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
                throws Exception {
            return getMessages(projection);
        }

        protected MatrixCursor getMessages(String[] projection) throws InterruptedException {
            BlockingQueue<List<MessageInfoHolder>> queue = new SynchronousQueue<List<MessageInfoHolder>>();

            // new code for integrated inbox, only execute this once as it will be processed afterwards via the listener
            SearchAccount integratedInboxAccount = SearchAccount.createUnifiedInboxAccount(getContext());
            MessagingController msgController = MessagingController.getInstance(getContext());

            msgController.searchLocalMessages(integratedInboxAccount.getRelatedSearch(),
                    new MessageInfoHolderRetrieverListener(queue));

            List<MessageInfoHolder> holders = queue.take();

            // TODO add sort order parameter
            Collections.sort(holders, new ReverseDateComparator());

            String[] projectionToUse;
            if (projection == null) {
                projectionToUse = DEFAULT_MESSAGE_PROJECTION;
            } else {
                projectionToUse = projection;
            }

            LinkedHashMap<String, FieldExtractor<MessageInfoHolder, ?>> extractors =
                    resolveMessageExtractors(projectionToUse, holders.size());
            int fieldCount = extractors.size();

            String[] actualProjection = extractors.keySet().toArray(new String[fieldCount]);
            MatrixCursor cursor = new MatrixCursor(actualProjection);

            for (MessageInfoHolder holder : holders) {
                Object[] o = new Object[fieldCount];

                int i = 0;
                for (FieldExtractor<MessageInfoHolder, ?> extractor : extractors.values()) {
                    o[i] = extractor.getField(holder);
                    i += 1;
                }

                cursor.addRow(o);
            }

            return cursor;
        }

        protected LinkedHashMap<String, FieldExtractor<MessageInfoHolder, ?>> resolveMessageExtractors(
                String[] projection, int count) {
            LinkedHashMap<String, FieldExtractor<MessageInfoHolder, ?>> extractors =
                    new LinkedHashMap<String, FieldExtractor<MessageInfoHolder, ?>>();

            for (String field : projection) {
                if (extractors.containsKey(field)) {
                    continue;
                }
                if (MessageColumns._ID.equals(field)) {
                    extractors.put(field, new IdExtractor());
                } else if (MessageColumns._COUNT.equals(field)) {
                    extractors.put(field, new CountExtractor<MessageInfoHolder>(count));
                } else if (MessageColumns.SUBJECT.equals(field)) {
                    extractors.put(field, new SubjectExtractor());
                } else if (MessageColumns.SENDER.equals(field)) {
                    extractors.put(field, new SenderExtractor());
                } else if (MessageColumns.SENDER_ADDRESS.equals(field)) {
                    extractors.put(field, new SenderAddressExtractor());
                } else if (MessageColumns.SEND_DATE.equals(field)) {
                    extractors.put(field, new SendDateExtractor());
                } else if (MessageColumns.PREVIEW.equals(field)) {
                    extractors.put(field, new PreviewExtractor());
                } else if (MessageColumns.URI.equals(field)) {
                    extractors.put(field, new UriExtractor());
                } else if (MessageColumns.DELETE_URI.equals(field)) {
                    extractors.put(field, new DeleteUriExtractor());
                } else if (MessageColumns.UNREAD.equals(field)) {
                    extractors.put(field, new UnreadExtractor());
                } else if (MessageColumns.ACCOUNT.equals(field)) {
                    extractors.put(field, new AccountExtractor());
                } else if (MessageColumns.ACCOUNT_COLOR.equals(field)) {
                    extractors.put(field, new AccountColorExtractor());
                } else if (MessageColumns.ACCOUNT_NUMBER.equals(field)) {
                    extractors.put(field, new AccountNumberExtractor());
                } else if (MessageColumns.HAS_ATTACHMENTS.equals(field)) {
                    extractors.put(field, new HasAttachmentsExtractor());
                } else if (MessageColumns.HAS_STAR.equals(field)) {
                    extractors.put(field, new HasStarExtractor());
                } else if (MessageColumns.INCREMENT.equals(field)) {
                    extractors.put(field, new IncrementExtractor());
                }
            }
            return extractors;
        }
    }