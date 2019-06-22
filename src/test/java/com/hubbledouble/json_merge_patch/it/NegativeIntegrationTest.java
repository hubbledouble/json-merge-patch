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

import com.hubbledouble.json_merge_patch.core.*;
import com.hubbledouble.json_merge_patch.processor.HTTPMethodProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class NegativeIntegrationTest {

    @Test
    public void patch_NodeElementLeafSameElementNameUpdate_ExpectOnlyLeafChange() {
        NodeBean<String> nodeBean = new NodeBean<>("one", new LeafBean<>("one"));
        String request = buildSimpleRequest("\"one\"", "\"two\"");
        HTTPMethodProcessor.patch(request, nodeBean);
        Assert.assertEquals("one", nodeBean.getElement());
        Assert.assertEquals("two", nodeBean.getLeaf().getElement());
    }

    @Test
    public void patch_NodeElementLeafSameElementNodeUpdate_ExpectOnlyNodeChange() {
        NodeBean<String> nodeBean = new NodeBean<>("one", new LeafBean<>("one"));
        String request = buildSimpleRequest("\"two\"", "\"one\"");
        HTTPMethodProcessor.patch(request, nodeBean);
        Assert.assertEquals("two", nodeBean.getElement());
        Assert.assertEquals("one", nodeBean.getLeaf().getElement());
    }

    @Test
    public void patch_ArrayWithNodeAndLeaf_ExpectAllChanges() {

        NodeBean<String> firstNodeBean = new NodeBean<>("one", new LeafBean<>("one"));
        NodeBean<String> secondNodeBean = new NodeBean<>("two", new LeafBean<>("two"));
        ArrayBeanNodes arrayBean = new ArrayBeanNodes(Arrays.asList(firstNodeBean, secondNodeBean));
        String request =
                "{ \"elements\" : [" +
                        "{" +
                        "  \"element\" : \"one\", " +
                        "  \"leaf\" : { " +
                        "    \"element\" : \"one\" " +
                        "     }" +
                        "}," +
                        "{" +
                        "  \"element\" : \"twoUpdated\", " +
                        "  \"leaf\" : { " +
                        "    \"element\" : \"twoUpdated\" " +
                        "     }" +
                        "}," +
                        "{" +
                        "  \"element\" : \"third\", " +
                        "  \"leaf\" : { " +
                        "    \"element\" : \"third\" " +
                        "     }" +
                        "}" +
                        "]}";

        HTTPMethodProcessor.patch(request, arrayBean);
        Assert.assertEquals(3, arrayBean.getElements().size());
        Assert.assertTrue(arrayBean.getElements().stream().anyMatch(i -> i.getElement().equals("one")));
        Assert.assertTrue(arrayBean.getElements().stream().anyMatch(i -> i.getElement().equals("twoUpdated")));
        Assert.assertTrue(arrayBean.getElements().stream().anyMatch(i -> i.getLeaf().getElement().equals("third")));
    }

    @Test
    public void patch_StartWithOnlyFirstNodeUpdateUntilThirdNode_ExpectFullNode() {

        FirstNode firstNode = new FirstNode("first", null);

        String request =
                "{" +
                        "\"name\" : \"first\", " +
                        "\"secondNode\" : { " +
                        "    \"name\" : \"second\", " +
                        "    \"thirdNode\" : { " +
                        "       \"name\" : \"third\" " +
                        "     }" +
                        " }" +
                        "}";

        HTTPMethodProcessor.patch(request, firstNode);
        Assert.assertEquals("third", firstNode.getSecondNode().getThirdNode().getName());

    }

    @Test
    public void patch_ThirdNodeUpdated_ExpectAllNodesRemainUntouched() {

        FirstNode firstNode = new FirstNode("first", new SecondNode("second", new ThirdNode("third")));

        String request =
                "{" +
                        "\"secondNode\" : { " +
                        "    \"thirdNode\" : { " +
                        "       \"name\" : \"thirdUpdated\" " +
                        "     }" +
                        " }" +
                        "}";

        HTTPMethodProcessor.patch(request, firstNode);
        Assert.assertEquals("first", firstNode.getName());
        Assert.assertEquals("second", firstNode.getSecondNode().getName());
        Assert.assertEquals("thirdUpdated", firstNode.getSecondNode().getThirdNode().getName());

    }

    @Test
    public void patch_UntilThirdNodeButEraseAndKeepOnlyFirstNode_ExpectOnlyFirst() {

        FirstNode firstNode = new FirstNode("first", new SecondNode("second", new ThirdNode("third")));

        String request =
                "{" +
                        "\"name\" : \"first\", " +
                        "\"secondNode\" : null " +
                        "}";

        HTTPMethodProcessor.patch(request, firstNode);
        Assert.assertEquals("first", firstNode.getName());
        Assert.assertNull(firstNode.getSecondNode());

    }

    private String buildSimpleRequest(String node, String leaf) {
        return
                "{" +
                        "\"element\" : " + node + ", " +
                        "\"leaf\" : { " +
                        "    \"element\" : " + leaf + " " +
                        "     }" +
                        "}";
    }

}