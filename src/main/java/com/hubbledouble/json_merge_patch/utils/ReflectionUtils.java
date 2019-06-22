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

package com.hubbledouble.json_merge_patch.utils;

import com.hubbledouble.json_merge_patch.constant.ErrorMessage;
import com.hubbledouble.json_merge_patch.exception.ErrorDetail;
import com.hubbledouble.json_merge_patch.exception.IllegalSetupException;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * Utilities for reflection related activities
 *
 * @author Jorge Saldivar
 */
public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static <T> String getDeclaringPackageName(T t) {
        String[] declaringPackage = null != t ? t.getClass().getPackage().getName().split("\\.") : new String[]{};
        if (declaringPackage.length > 1) return declaringPackage[0] + "." + declaringPackage[1];
        throw new IllegalSetupException(
                ErrorMessage.SETUP,
                Collections.singletonList(new ErrorDetail("package", "Classes living on root level not supported, please declare custom packages (Eg. com.domain)")));
    }

    public static boolean isFieldFromJavaSource(Field field) {
        return null != field ?
                isClassFromJavaSource(field.getDeclaringClass()) : false;
    }

    public static <T> boolean isClassFromJavaSource(Class<T> tClass) {
        return null != tClass ?
                tClass.getName().startsWith("java") : false;
    }

    public static boolean isFieldFromDeclaringClass(Field field, String declaringPackageName) {
        return null != field ?
                field.getDeclaringClass().getName().contains(declaringPackageName) && !isFieldFromJavaSource(field) :
                false;
    }

}
