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

package com.hubbledouble.json_merge_patch.validator;

import com.hubbledouble.json_merge_patch.constant.ErrorMessage;
import com.hubbledouble.json_merge_patch.exception.ErrorDetail;
import com.hubbledouble.json_merge_patch.exception.ValidationException;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Request validator.
 *
 * @author Jorge Saldivar
 */
public class RequestValidator {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private RequestValidator() {
    }

    public static <T> void initialValidation(T t) {
        if (null == t)
            throw new ValidationException(
                    ErrorMessage.VALIDATION,
                    Collections.singletonList(new ErrorDetail("object", "Object must be initialized")));
    }

    /**
     * Verifies if object is not breaking any javax constraint.
     * Fail fast mechanism. If any exception is found, throw first constraint exception
     *
     * @param t
     * @param <T>
     */
    public static <T> void finalValidation(T t) {

        try {

            List<ErrorDetail> jsonMergePatchErrorDetails = new ArrayList<>();
            VALIDATOR
                    .validate(t)
                    .forEach(violation ->
                            jsonMergePatchErrorDetails
                                    .add(new ErrorDetail(
                                            violation.getPropertyPath().toString(), violation.getMessage())));
            if (!jsonMergePatchErrorDetails.isEmpty())
                throw new ValidationException(
                        ErrorMessage.VALIDATION, jsonMergePatchErrorDetails);

        } catch (Exception e) {
            throw new ValidationException(
                    ErrorMessage.VALIDATION,
                    Collections.singletonList(new ErrorDetail("unknown", e.getMessage())));

        }
    }

}