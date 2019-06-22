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

import com.hubbledouble.json_merge_patch.constant.ErrorMessage;
import com.hubbledouble.json_merge_patch.exception.ErrorDetail;
import com.hubbledouble.json_merge_patch.exception.FieldProcessorException;
import com.hubbledouble.json_merge_patch.exception.FieldUpdateException;
import com.hubbledouble.json_merge_patch.exception.ValidationException;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Field processor updater
 *
 * @author Jorge Saldivar
 */
class FieldProcessor {

    private FieldProcessor() {
    }

    static <T> void updateRegularField(T object, T request, Field field, String fieldName, String pathLocation) {

        if (pathLocation.equalsIgnoreCase(fieldName))
            FieldProcessor.updateValue(object, request, field);

    }

    static <T> void updateObjectField(T object, T request, Field field, String fieldName, String pathLocation) {

        try {

            field.setAccessible(true);
            if (pathLocation.equalsIgnoreCase(fieldName) && (null == field.get(object) || field.get(object) instanceof Map))
                FieldProcessor.updateValue(object, request, field);

        } catch (IllegalAccessException e) {

            throw new FieldProcessorException(
                    ErrorMessage.INTERNAL,
                    Collections.singletonList(new ErrorDetail(field.getName(), e.getMessage())));

        }
    }

    static <T> void updateValue(T object, T requestObject, Field field) {

        try {

            field.setAccessible(true);
            if (null != requestObject)
                field.set(object, field.get(requestObject));

        } catch (Exception e) {

            throw new FieldUpdateException(
                    ErrorMessage.INTERNAL,
                    Collections.singletonList(new ErrorDetail(field.getName(), e.getMessage())));

        }

    }

    static void replaceMap(Map<Object, Object> object, Map<Object, Object> request) {

        if (null != object) {
            object.clear();
            if (null != request)
                for (Map.Entry<Object, Object> entry : request.entrySet())
                    object.put(
                            entry.getKey(),
                            entry.getValue());
        } else
            throw new ValidationException(
                    ErrorMessage.VALIDATION,
                    Collections.singletonList(new ErrorDetail("request", "Map cannot be replaced if it's not initialized")));

    }

    static void replaceCollection(Collection<Object> object, Collection<Object> request) {

        if (null != object) {
            object.clear();
            if (null != request)
                object.addAll(request);
        } else
            throw new ValidationException(
                    ErrorMessage.VALIDATION,
                    Collections.singletonList(new ErrorDetail("request", "Collection cannot be replaced if it's not initialized")));

    }

}