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

package com.hubbledouble.json_merge_patch.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubbledouble.json_merge_patch.exception.JsonMapperException;
import com.hubbledouble.json_merge_patch.constant.ErrorMessage;
import com.hubbledouble.json_merge_patch.exception.ErrorDetail;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Jorge Saldivar
 */
public class JSONMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JSONMapper() {
    }

    public static <T> T jsonStringToObject(String request, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(request, clazz);
        } catch (IOException e) {
            throw new JsonMapperException(
                    ErrorMessage.MAPPER,
                    Collections.singletonList(new ErrorDetail("request", e.getMessage())));
        }
    }

    public static JsonNode jsonStringToJsonNode(String request) {
        try {
            return OBJECT_MAPPER.readTree(request);
        } catch (IOException e) {
            throw new JsonMapperException(
                    ErrorMessage.MAPPER,
                    Collections.singletonList(new ErrorDetail("request", e.getMessage())));
        }
    }

}