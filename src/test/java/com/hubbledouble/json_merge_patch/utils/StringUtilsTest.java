/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2019, HubbleDouble
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.hubbledouble.json_merge_patch.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void build_ParentAndChildNotNull_CombineWithDot() {
        Assert.assertEquals("parent.child", StringUtils.build("parent", "child"));
    }

    @Test
    public void build_ParentNotNullChildNull_ReturnParent() {
        Assert.assertEquals("parent", StringUtils.build("parent", null));
    }

    @Test
    public void build_ParentNullChildNotNull_ReturnChild() {
        Assert.assertEquals("child", StringUtils.build(null, "child"));
    }

    @Test
    public void build_ParentNullChildNull_ReturnNull() {
        Assert.assertNull(StringUtils.build(null, null));
    }

    @Test
    public void isEmpty_Null_True() {
        Assert.assertTrue(StringUtils.isEmptyOrNull(null));
    }

    @Test
    public void isEmpty_Empty_True() {
        Assert.assertTrue(StringUtils.isEmptyOrNull(""));
    }

    @Test
    public void isEmpty_Blank_True() {
        Assert.assertTrue(StringUtils.isEmptyOrNull(" "));
    }

    @Test
    public void isEmpty_NotEmpty_False() {
        Assert.assertFalse(StringUtils.isEmptyOrNull("something"));
    }

}