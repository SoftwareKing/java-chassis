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
package com.huawei.paas.cse.swagger.invocation.response;

import org.junit.Assert;
import org.junit.Test;

import com.huawei.paas.cse.core.exception.CommonExceptionData;
import com.huawei.paas.cse.swagger.generator.core.SwaggerGenerator;
import com.huawei.paas.cse.swagger.generator.core.unittest.UnitTestSwaggerUtils;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author
 * @version  [版本号, 2017年5月3日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TestResponsesMeta {
    class ResponseMetaImpl {
        @ApiResponses({@ApiResponse(code = 400, response = String.class, message = ""),
                @ApiResponse(
                        code = 401,
                        response = long.class,
                        message = "",
                        responseHeaders = {@ResponseHeader(name = "h1", response = int.class)})
        })
        public int add(int x, int y) {
            return x + y;
        }

    }

    @Test
    public void test() {
        SwaggerGenerator generator = UnitTestSwaggerUtils.generateSwagger(ResponseMetaImpl.class);
        Swagger swagger = generator.getSwagger();
        Operation operation = swagger.getPath("/add").getPost();

        ResponsesMeta meta = new ResponsesMeta();
        meta.init(null, "gen", swagger, operation, int.class);

        ResponseMeta resp = meta.findResponseMeta(200);
        Assert.assertEquals(int.class, resp.getJavaType().getRawClass());

        resp = meta.findResponseMeta(201);
        Assert.assertEquals(int.class, resp.getJavaType().getRawClass());

        resp = meta.findResponseMeta(400);
        Assert.assertEquals(String.class, resp.getJavaType().getRawClass());

        resp = meta.findResponseMeta(401);
        Assert.assertEquals(Long.class, resp.getJavaType().getRawClass());
        Assert.assertEquals(Integer.class, resp.getHeaders().get("h1").getRawClass());

        resp = meta.findResponseMeta(500);
        Assert.assertEquals(CommonExceptionData.class, resp.getJavaType().getRawClass());
    }
}
