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

package com.hubbledouble.json_merge_patch.it;

import com.hubbledouble.json_merge_patch.core.ArrayBean;
import com.hubbledouble.json_merge_patch.exception.JsonMergePatchException;
import com.hubbledouble.json_merge_patch.exception.ValidationException;
import com.hubbledouble.json_merge_patch.processor.HTTPMethodProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArrayIntegrationTest {

    @Test
    public void patch_Array_UpdateArray() {
        testArray(new LinkedList<>(Arrays.asList("one", "two")));
    }

    @Test
    public void patch_EmptyArray_UpdateArray() {
        testArray(new LinkedList<>());
    }

    @Test
    public void patch_NullArray_UpdateArray() {
        testArray(null);
    }

    @Test
    public void patch_ArrayToEmptyArray_ExpectEmptyArray() {
        String request =
                "{" +
                        "\"elements\" : [] " +
                        "}";

        ArrayBean<String> bean = new ArrayBean<>(Arrays.asList("one"));
        HTTPMethodProcessor.patch(request, bean);
        Assert.assertNotNull(bean);
        Assert.assertEquals(0, bean.getElements().size());
    }

    @Test
    public void patch_ArrayToNull_ExpectNull() {
        String request =
                "{" +
                        "\"elements\" : null " +
                        "}";

        ArrayBean<String> bean = new ArrayBean<>(Arrays.asList("one"));
        HTTPMethodProcessor.patch(request, bean);
        Assert.assertNull(bean.getElements());
    }

    @Test
    public void patch_PlainArray() {
        List<String> object = new ArrayList<>();
        String request =
                "[" +
                        "\"value\" " +
                        "]";

        HTTPMethodProcessor.patch(request, object);
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("value", object.get(0));

    }

    @Test(expected = ValidationException.class)
    public void patch_PlainArrayNull_ThrowException() {
        List<String> object = null;
        String request =
                "[" +
                        "\"value\" " +
                        "]";

        HTTPMethodProcessor.patch(request, object);

    }

    @Test(expected = JsonMergePatchException.class)
    public void patch_PlainArrayRequestNull_ThrowException() {
        List<String> object = new ArrayList<>();
        String request = null;

        HTTPMethodProcessor.patch(request, object);

    }

    private void testArray(List<String> initialValue) {

        String request =
                "{" +
                        "\"elements\" : [\"three\"] " +
                        "}";

        ArrayBean<String> bean = new ArrayBean<>();
        bean.setElements(initialValue);
        HTTPMethodProcessor.patch(request, bean);
        Assert.assertEquals(1, bean.getElements().size());
        Assert.assertEquals("three", bean.getElements().get(0));

    }

}