package com.fsck.k9.search;

/**
 * This class stores search conditions. It's basically a boolean expression binary tree.
 * The output will be SQL queries ( obtained by traversing inorder ).
 *
 * TODO removing conditions from the tree
 * TODO implement NOT as a node again
 */
public enum Operator {
        AND, OR, CONDITION;
    }