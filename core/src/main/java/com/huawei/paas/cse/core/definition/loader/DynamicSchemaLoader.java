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

package com.huawei.paas.cse.core.definition.loader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.huawei.paas.cse.core.CseContext;
import com.huawei.paas.cse.core.definition.SchemaMeta;
import com.huawei.paas.cse.serviceregistry.RegistryUtils;
import com.huawei.paas.foundation.common.config.PaaSResourceUtils;

/**
 * 场景：
 * 1.consumer
 *   网管调用产品
 *   网管事先不知道产品对应的微服务名
 *   产品注册到网管后，网管根据注册信息，进行契约注册
 * 2.producer
 *   需要支持在不同的产品中部署为不同的微服务名
 *   微服务名是由环境变量等等方式注入的
 *   此时可以在BootListener中进行注册（必须在producer初始化之前注册契约）
 *
 * @author   
 * @version  [版本号, 2017年3月30日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DynamicSchemaLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicSchemaLoader.class);

    public static final DynamicSchemaLoader INSTANCE = new DynamicSchemaLoader();

    /**
     * 动态注册指定目录下的schema契约到当前服务
     * @param schemaLocation eg. "classpath*:schemas/*.yaml"
     * @throws Exception
     */
    public void registerSchemas(String schemaLocation) {
        registerSchemas(RegistryUtils.getMicroservice().getServiceName(), schemaLocation);
    }

    /**
     * 动态注册指定目录下的schema契约到指定服务
     * @param microserviceName
     * @param schemaLocation eg. "classpath*:schemas/*.yaml"
     * @throws Exception
     */
    public void registerSchemas(String microserviceName, String schemaLocation) {
        LOGGER.info("dynamic register schemas for {} in {}", microserviceName, schemaLocation);

        List<SchemaMeta> schemaMetaList = new ArrayList<>();
        Resource[] resArr = PaaSResourceUtils.getResources(schemaLocation);
        for (Resource resource : resArr) {
            SchemaMeta schemaMeta =
                CseContext.getInstance().getSchemaLoader().registerSchema(microserviceName, resource);

            schemaMetaList.add(schemaMeta);
        }

        CseContext.getInstance().getSchemaListenerManager().notifySchemaListener(schemaMetaList);
    }
}
