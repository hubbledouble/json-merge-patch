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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.hubbledouble.json_merge_patch.constant.ErrorMessage;
import com.hubbledouble.json_merge_patch.exception.ErrorDetail;
import com.hubbledouble.json_merge_patch.exception.FieldProcessorException;
import com.hubbledouble.json_merge_patch.utils.ReflectionUtils;
import com.hubbledouble.json_merge_patch.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Processor for node traversal
 *
 * @author Jorge Saldivar
 */
class NodeProcessor {

    private NodeProcessor() {
    }

    static <T> void iterateJsonTree(T object,
                                    T request,
                                    Iterator<Map.Entry<String, JsonNode>> requestFields,
                                    String pathLocation,
                                    String declaringPackageName) {

        while (requestFields.hasNext()) {

            Map.Entry<String, JsonNode> entry = requestFields.next();
            final String currentPath = StringUtils.build(pathLocation, entry.getKey());
            iterateJsonNode(object, request, entry.getValue(), currentPath, declaringPackageName);

        }

    }

    private static <T> void iterateJsonNode(T object,
                                            T request,
                                            JsonNode requestField,
                                            String pathLocation,
                                            String declaringPackageName) {

        if (requestField.isValueNode() || JsonNodeType.ARRAY.equals(requestField.getNodeType()))
            processFields(object, request, pathLocation, null, declaringPackageName, false);

        else if (JsonNodeType.OBJECT.equals(requestField.getNodeType())) {
            processFields(object, request, pathLocation, null, declaringPackageName, true);
            iterateJsonTree(object, request, requestField.fields(), pathLocation, declaringPackageName);
        }

    }

    private static <T> void processFields(T object,
                                          T request,
                                          String pathLocation,
                                          String parentFieldName,
                                          String declaringPackageName,
                                          boolean isNodeObject) {

        for (Field field : object.getClass().getDeclaredFields())
            processField(object, request, field, pathLocation, parentFieldName, declaringPackageName, isNodeObject);

    }

    private static <T> void processField(T object,
                                         T request,
                                         Field field,
                                         String pathLocation,
                                         String parentFieldName,
                                         String declaringPackageName,
                                         boolean isNodeObject) {

        if (ReflectionUtils.isFieldFromDeclaringClass(field, declaringPackageName)) {

            final String fieldName = StringUtils.build(parentFieldName, field.getName());
            if (isNodeObject)
                FieldProcessor.updateObjectField(object, request, field, fieldName, pathLocation);
            else
                FieldProcessor.updateRegularField(object, request, field, fieldName, pathLocation);

            exploreNestedField(object, request, field, fieldName, pathLocation, declaringPackageName, isNodeObject);

        }
    }

    private static <T> void exploreNestedField(T object,
                                               T request,
                                               Field field,
                                               String fieldName,
                                               String pathLocation,
                                               String declaringPackageName,
                                               boolean isNodeObject) {

        try {

            field.setAccessible(true);
            if (null != field.getType().getDeclaredFields() && null != field.get(object))
                if (null != request)
                    processFields(field.get(object), field.get(request), pathLocation, fieldName, declaringPackageName, isNodeObject);
                else
                    processFields(field.get(object), null, pathLocation, fieldName, declaringPackageName, isNodeObject);

        } catch (IllegalAccessException e) {

            throw new FieldProcessorException(
                    ErrorMessage.INTERNAL,
                    Collections.singletonList(new ErrorDetail(field.getName(), e.getMessage())));

        }

    }

}