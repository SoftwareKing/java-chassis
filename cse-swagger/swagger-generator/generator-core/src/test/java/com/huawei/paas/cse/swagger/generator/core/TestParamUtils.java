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

package com.huawei.paas.cse.swagger.generator.core;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.huawei.paas.cse.swagger.generator.core.utils.ClassUtils;

import io.swagger.models.parameters.Parameter;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author
 * @version  [版本号, 2017年4月10日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TestParamUtils {
    @Test
    public void testGetRawJsonType() {
        Parameter param = Mockito.mock(Parameter.class);
        Map<String, Object> extensions = new HashMap<>();
        when(param.getVendorExtensions()).thenReturn(extensions);

        extensions.put(SwaggerConst.EXT_RAW_JSON_TYPE, true);
        Assert.assertTrue(ClassUtils.isRawJsonType(param));

        extensions.put(SwaggerConst.EXT_RAW_JSON_TYPE, "test");
        Assert.assertFalse(ClassUtils.isRawJsonType(param));
    }
}
