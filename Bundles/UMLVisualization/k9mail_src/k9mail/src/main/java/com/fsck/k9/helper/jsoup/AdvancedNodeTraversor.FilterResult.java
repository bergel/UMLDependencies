/*
 * The MIT License
 *
 * Â© 2009-2017, Jonathan Hedley <jonathan@hedley.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.fsck.k9.helper.jsoup;


import org.jsoup.select.NodeTraversor;


/**
 * Depth-first node traversor.
 * <p>
 * Based on {@link NodeTraversor}, but supports skipping sub trees, removing nodes, and stopping the traversal at any
 * point.
 * </p><p>
 * This is an enhancement of the <a href="https://github.com/jhy/jsoup/pull/849">jsoup pull request 'Improved node
 * traversal'</a> by <a href="https://github.com/kno10">Erich Schubert</a>.
 * </p>
 */
public enum FilterResult {
        /**
         * Processing the tree was completed.
         */
        ENDED,
        /**
         * Processing was stopped.
         */
        STOPPED,
        /**
         * Processing the tree was completed and the root node was removed.
         */
        ROOT_REMOVED
    }