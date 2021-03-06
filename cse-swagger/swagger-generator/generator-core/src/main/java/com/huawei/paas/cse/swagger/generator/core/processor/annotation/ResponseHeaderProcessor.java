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
package com.huawei.paas.cse.swagger.generator.core.processor.annotation;

import com.huawei.paas.cse.swagger.generator.core.MethodAnnotationProcessor;
import com.huawei.paas.cse.swagger.generator.core.OperationGenerator;
import com.huawei.paas.cse.swagger.generator.core.processor.annotation.models.ResponseHeaderConfig;

import io.swagger.annotations.ResponseHeader;
import io.swagger.models.properties.Property;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author
 * @version  [版本号, 2017年4月20日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ResponseHeaderProcessor implements MethodAnnotationProcessor {

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(Object annotation, OperationGenerator operationGenerator) {
        ResponseHeader responseHeader = (ResponseHeader) annotation;

        ResponseHeaderConfig config = AnnotationUtils.convert(responseHeader);
        if (config != null) {
            Property property =
                AnnotationUtils.generateResponseHeaderProperty(operationGenerator.getSwagger(), config);
            operationGenerator.addResponseHeader(config.getName(), property);
        }
    }
}
