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

package com.hubbledouble.json_merge_patch.processor;

import com.hubbledouble.json_merge_patch.core.*;
import com.hubbledouble.json_merge_patch.exception.FieldUpdateException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class FieldProcessorTest {

    @Test
    public void updateValue_String_UpdateStringTest() throws Exception {
        FirstNode firstNode = new FirstNode();
        FieldProcessor.updateValue(
                firstNode,
                new FirstNode("name", new SecondNode()),
                firstNode.getClass().getDeclaredField("secondNode"));
        Assert.assertNotNull(firstNode.getSecondNode());
    }

    @Test
    public void updateValue_String_UpdateString() throws Exception {
        StringBean bean = new StringBean(null);
        FieldProcessor.updateValue(
                bean,
                new StringBean("updated"),
                bean.getClass().getDeclaredField("name"));
        Assert.assertEquals("updated", bean.getName());
    }

    @Test
    public void updateValue_Integer_UpdateNumber() throws Exception {
        NumbersBean bean = new NumbersBean(null, null, null, null);
        FieldProcessor.updateValue(
                bean,
                new NumbersBean(1, null, null, null),
                bean.getClass().getDeclaredField("integerNumber"));
        Assert.assertEquals("1", bean.getIntegerNumber().toString());
    }

    @Test
    public void updateValue_Long_UpdateNumber() throws Exception {
        NumbersBean bean = new NumbersBean(null, null, null, null);
        FieldProcessor.updateValue(
                bean,
                new NumbersBean(null, 1l, null, null),
                bean.getClass().getDeclaredField("longNumber"));
        Assert.assertEquals("1", bean.getLongNumber().toString());
    }

    @Test
    public void updateValue_Float_UpdateNumber() throws Exception {
        NumbersBean bean = new NumbersBean(null, null, null, null);
        FieldProcessor.updateValue(
                bean,
                new NumbersBean(null, null, 1f, null),
                bean.getClass().getDeclaredField("floatNumber"));
        Assert.assertEquals("1.0", bean.getFloatNumber().toString());
    }

    @Test
    public void updateValue_Double_UpdateNumber() throws Exception {
        NumbersBean bean = new NumbersBean(null, null, null, null);
        FieldProcessor.updateValue(
                bean,
                new NumbersBean(null, null, null, 1d),
                bean.getClass().getDeclaredField("doubleNumber"));
        Assert.assertEquals("1.0", bean.getDoubleNumber().toString());
    }

    @Test
    public void updateValue_Boolean_UpdateBoolean() throws Exception {
        BooleanBean bean = new BooleanBean(null);
        FieldProcessor.updateValue(
                bean,
                new BooleanBean(true),
                bean.getClass().getDeclaredField("element"));
        Assert.assertTrue(bean.getElement());
    }

    @Test
    public void updateValue_Array_UpdateArray() throws Exception {
        ArrayBean<String> bean = new ArrayBean<>(null);
        FieldProcessor.updateValue(
                bean,
                new ArrayBean<>(Arrays.asList("firstValue")),
                bean.getClass().getDeclaredField("elements"));
        Assert.assertEquals(1, bean.getElements().size());
        Assert.assertEquals("firstValue", bean.getElements().get(0));
    }

    @Test
    public void updateValue_NodeAndLeaf_UpdateNodeAndLeaf() throws Exception {
        NodeBean<String> bean = new NodeBean<>(null, new LeafBean<>(null));
        NodeBean<String> requestBean = new NodeBean<>("nodeValue", new LeafBean<>("leafValue"));

        FieldProcessor.updateValue(bean, requestBean, bean.getClass().getDeclaredField("element"));
        FieldProcessor.updateValue(bean, requestBean, bean.getClass().getDeclaredField("leaf"));

        Assert.assertEquals("nodeValue", bean.getElement());
        Assert.assertEquals("leafValue", bean.getLeaf().getElement());
    }

    @Test(expected = FieldUpdateException.class)
    public void updateValue_ObjectDifferentObject_ThrowException() throws Exception {
        StringBean stringBean = new StringBean();
        NumbersBean numbersBean = new NumbersBean();
        FieldProcessor.updateValue(stringBean, numbersBean, stringBean.getClass().getDeclaredField("name"));
    }

}