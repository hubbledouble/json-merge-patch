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

import com.hubbledouble.json_merge_patch.core.MapBean;
import com.hubbledouble.json_merge_patch.processor.HTTPMethodProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapIntegrationTest {

    @Test
    public void patch_MapNotEmpty_UpdateMap() {
        String request =
                "{" +
                        "\"elements\" : {" +
                        "\"two\" : \"2\", " +
                        "\"three\" : \"3\"" +
                        "} " +
                        "}";

        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        MapBean bean = new MapBean(map);
        HTTPMethodProcessor.patch(request, bean);
        Assert.assertTrue(
                bean.getElements()
                        .entrySet()
                        .stream()
                        .anyMatch(i -> i.getKey().equals("two") && i.getValue().equals("2")));
        Assert.assertTrue(
                bean.getElements()
                        .entrySet()
                        .stream()
                        .anyMatch(i -> i.getKey().equals("three") && i.getValue().equals("3")));
        Assert.assertEquals(2, bean.getElements().size());
    }

    @Test
    public void patch_InitialMapNull_UpdateMap() {
        String request =
                "{" +
                        "\"elements\" : {" +
                        "\"two\" : \"2\", " +
                        "\"three\" : \"3\"" +
                        "} " +
                        "}";

        MapBean bean = new MapBean();
        HTTPMethodProcessor.patch(request, bean);
        Assert.assertTrue(
                bean.getElements()
                        .entrySet()
                        .stream()
                        .anyMatch(i -> i.getKey().equals("two") && i.getValue().equals("2")));
        Assert.assertTrue(
                bean.getElements()
                        .entrySet()
                        .stream()
                        .anyMatch(i -> i.getKey().equals("three") && i.getValue().equals("3")));
        Assert.assertEquals(2, bean.getElements().size());
    }

    @Test
    public void patch_ResultMapEmpty_UpdateMap() {
        String request =
                "{" +
                        "\"elements\" : {} " +
                        "}";

        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        MapBean bean = new MapBean(map);
        HTTPMethodProcessor.patch(request, bean);
        Assert.assertEquals(0, bean.getElements().size());
    }

    @Test
    public void patch_ResultMapNull_UpdateMap() {
        String request =
                "{" +
                        "\"elements\" : null " +
                        "}";

        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        MapBean bean = new MapBean(map);
        HTTPMethodProcessor.patch(request, bean);
        Assert.assertNull(bean.getElements());
    }

    @Test
    public void patch_RegularMapNotEmpty_UpdateMap() {
        String request =
                "{" +
                        "\"two\" : \"2\", " +
                        "\"three\" : \"3\"" +
                        "}";

        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        HTTPMethodProcessor.patch(request, map);
    }

}