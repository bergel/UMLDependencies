package com.fsck.k9.provider;


public static class CountExtractor<T> implements FieldExtractor<T, Integer> {
        private Integer count;

        public CountExtractor(int count) {
            this.count = count;
        }

        @Override
        public Integer getField(T source) {
            return count;
        }
    }