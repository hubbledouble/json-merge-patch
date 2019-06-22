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
import com.hubbledouble.json_merge_patch.exception.JsonMergePatchException;
import com.hubbledouble.json_merge_patch.mapper.JSONMapper;
import com.hubbledouble.json_merge_patch.validator.RequestValidator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.hubbledouble.json_merge_patch.utils.ReflectionUtils.getDeclaringPackageName;

/**
 * Implementation for Json Merge Patch based on RFC-7386.
 *
 * @author Jorge Saldivar
 * @see <a href="https://tools.ietf.org/html/rfc7386">RFC-7386</a>
 */
public class HTTPMethodProcessor {

    private HTTPMethodProcessor() {
    }

    /**
     * Method to patch an object based on a JSON partial request.
     * For more information visit the specs documentation <a href="https://tools.ietf.org/html/rfc7386">RFC-7386</a>
     *
     * <pre>
     * Skeleton example:
     * <code>
     *
     *     public T patch(String json, String pathParamId){
     *          T object = repository.findById(pathParamId);
     *          HTTPMethodProcessor.patch(json, object);
     *          repository.save(object);
     *     }
     *
     * </code>
     * </pre>
     *
     * @param request - partial json request
     * @param object  - object where the partial json request will merge
     * @param <T>
     * @throws JsonMergePatchException
     */
    public static <T> void patch(String request, T object) {

        try {

            RequestValidator.initialValidation(object);
            processMapCollectionOrObject(
                    request,
                    JSONMapper.jsonStringToObject(request, object.getClass()),
                    object);

            RequestValidator.finalValidation(object);

        } catch (JsonMergePatchException e) {
            throw e;

        } catch (Exception e) {
            throw new JsonMergePatchException(
                    ErrorMessage.INTERNAL,
                    Collections.singletonList(new ErrorDetail("unknown", e.getMessage())));
        }
    }

    private static <T> void processMapCollectionOrObject(String originalRequest, T request, T object) {

        if (object instanceof Map)
            FieldProcessor.replaceMap((Map) object, (Map) request);

        else if (object instanceof Collection)
            FieldProcessor.replaceCollection((Collection) object, (Collection) request);

        else
            NodeProcessor.iterateJsonTree(
                    object,
                    request,
                    JSONMapper.jsonStringToJsonNode(originalRequest).fields(),
                    null,
                    getDeclaringPackageName(object));

    }

}