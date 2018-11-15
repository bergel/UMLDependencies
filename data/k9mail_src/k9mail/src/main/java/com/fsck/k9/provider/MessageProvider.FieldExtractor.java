package com.fsck.k9.provider;


public interface FieldExtractor<T, K> {
        K getField(T source);
    }