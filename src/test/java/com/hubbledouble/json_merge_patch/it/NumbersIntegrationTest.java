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

import com.hubbledouble.json_merge_patch.core.NumbersBean;
import com.hubbledouble.json_merge_patch.processor.HTTPMethodProcessor;
import org.junit.Assert;
import org.junit.Test;

public class NumbersIntegrationTest {

    @Test
    public void patch_NumberObjectsString_UpdateNumbers() {

        String request =
                "{" +
                        "\"integerNumber\" : \"5\", " +
                        "\"longNumber\" : \"6\", " +
                        "\"floatNumber\" : \"7.0\", " +
                        "\"doubleNumber\" : \"8.0\" " +
                        "}";
        testNumbers(1, 2l, 3f, 4d, request);
    }

    @Test
    public void patch_NumberObjectsNotString_UpdateNumbers() {

        String request =
                "{" +
                        "\"integerNumber\" : 5, " +
                        "\"longNumber\" : 6, " +
                        "\"floatNumber\" : 7.0, " +
                        "\"doubleNumber\" : 8.0 " +
                        "}";
        testNumbers(1, 2l, 3f, 4d, request);
    }

    @Test
    public void patch_InitialNullNumbers_UpdateNumbers() {

        String request =
                "{" +
                        "\"integerNumber\" : 5, " +
                        "\"longNumber\" : 6, " +
                        "\"floatNumber\" : 7.0, " +
                        "\"doubleNumber\" : 8.0 " +
                        "}";
        testNumbers(null, null, null, null, request);
    }


    private void testNumbers(Integer integerNumber,
                             Long longNumber,
                             Float floatNumber,
                             Double doubleNumber,
                             String request) {

        NumbersBean numbersBean = new NumbersBean(integerNumber, longNumber, floatNumber, doubleNumber);

        HTTPMethodProcessor.patch(request, numbersBean);
        Assert.assertEquals(new Integer(5), numbersBean.getIntegerNumber());
        Assert.assertEquals(new Long(6), numbersBean.getLongNumber());
        Assert.assertEquals(new Float(7), numbersBean.getFloatNumber());
        Assert.assertEquals(new Double(8), numbersBean.getDoubleNumber());

    }

}