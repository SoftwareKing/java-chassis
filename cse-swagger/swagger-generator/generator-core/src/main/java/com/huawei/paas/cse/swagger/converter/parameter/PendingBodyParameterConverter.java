/*
 * Copyright 2017 Huawei Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.paas.cse.swagger.converter.parameter;

import com.fasterxml.jackson.databind.JavaType;
import com.huawei.paas.cse.swagger.converter.Converter;
import com.huawei.paas.cse.swagger.converter.ConverterMgr;
import com.huawei.paas.cse.swagger.extend.parameter.PendingBodyParameter;

import io.swagger.models.Swagger;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author
 * @version  [版本号, 2017年3月30日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PendingBodyParameterConverter implements Converter {

    /**
     * {@inheritDoc}
     */
    @Override
    public JavaType convert(ClassLoader classLoader, String packageName, Swagger swagger, Object def) {
        PendingBodyParameter param = (PendingBodyParameter) def;
        return ConverterMgr.findJavaType(classLoader, packageName, swagger, param.getProperty());
    }

}
