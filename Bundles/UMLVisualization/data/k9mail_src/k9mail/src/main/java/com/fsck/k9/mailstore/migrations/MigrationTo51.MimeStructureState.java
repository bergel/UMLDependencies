package com.fsck.k9.mailstore.migrations;


import android.content.ContentValues;
import android.support.annotation.VisibleForTesting;
@VisibleForTesting
    static class MimeStructureState {
        private final Long rootPartId;
        private final Long prevParentId;
        private final long parentId;
        private final int nextOrder;

        // just some diagnostic state to make sure all operations are called in order
        private boolean isValuesApplied;
        private boolean isStateAdvanced;


        private MimeStructureState(Long rootPartId, Long prevParentId, long parentId, int nextOrder) {
            this.rootPartId = rootPartId;
            this.prevParentId = prevParentId;
            this.parentId = parentId;
            this.nextOrder = nextOrder;
        }

        public static MimeStructureState getNewRootState() {
            return new MimeStructureState(null, null, -1, 0);
        }

        public MimeStructureState nextChild(long newPartId) {
            if (!isValuesApplied || isStateAdvanced) {
                throw new IllegalStateException("next* methods must only be called once");
            }
            isStateAdvanced = true;

            if (rootPartId == null) {
                return new MimeStructureState(newPartId, null, -1, nextOrder+1);
            }
            return new MimeStructureState(rootPartId, prevParentId, parentId, nextOrder+1);
        }

        public MimeStructureState nextMultipartChild(long newPartId) {
            if (!isValuesApplied || isStateAdvanced) {
                throw new IllegalStateException("next* methods must only be called once");
            }
            isStateAdvanced = true;

            if (rootPartId == null) {
                return new MimeStructureState(newPartId, parentId, newPartId, nextOrder+1);
            }
            return new MimeStructureState(rootPartId, parentId, newPartId, nextOrder+1);
        }

        public void applyValues(ContentValues cv) {
            if (isValuesApplied || isStateAdvanced) {
                throw new IllegalStateException("applyValues must be called exactly once, after a call to next*");
            }
            if (rootPartId != null && parentId == -1L) {
                throw new IllegalStateException("applyValues must not be called after a root nextChild call");
            }
            isValuesApplied = true;

            if (rootPartId != null) {
                cv.put("root", rootPartId);
            }
            cv.put("parent", parentId);
            cv.put("seq", nextOrder);
        }

        public MimeStructureState popParent() {
            if (prevParentId == null) {
                throw new IllegalStateException("popParent must only be called if parent depth is >= 2");
            }
            return new MimeStructureState(rootPartId, null, prevParentId, nextOrder);
        }
    }