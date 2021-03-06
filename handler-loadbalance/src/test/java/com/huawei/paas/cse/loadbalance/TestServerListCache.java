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

package com.huawei.paas.cse.loadbalance;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.huawei.paas.cse.core.Transport;
import com.huawei.paas.cse.serviceregistry.RegistryUtils;
import com.huawei.paas.cse.serviceregistry.api.registry.Microservice;
import com.huawei.paas.cse.serviceregistry.cache.CacheEndpoint;
import com.huawei.paas.cse.serviceregistry.cache.InstanceCache;
import com.huawei.paas.cse.serviceregistry.cache.InstanceCacheManager;
import com.netflix.loadbalancer.Server;

import mockit.Mock;
import mockit.MockUp;

public class TestServerListCache {

    private ServerListCache instance = null;

    private Transport transport = null;

    private void mockTestCases() {
        final Microservice microService = new Microservice();
        microService.setAppId("appId");
        new MockUp<RegistryUtils>() {
            @Mock
            public Microservice getMicroservice() {
                return microService;
            }

            @Mock
            private Microservice createMicroserviceFromDefinition() {
                return microService;
            }
        };

        new MockUp<InstanceCacheManager>() {
            @Mock
            public InstanceCache getOrCreate(String appId, String microserviceName, String microserviceVersionRule) {
                return null;
            }

        };

        transport = Mockito.mock(Transport.class);

    }

    @Before
    public void setUp() throws Exception {
        mockTestCases();
        instance = new ServerListCache("appId", "microserviceName", "microserviceVersionRule", "transportName");
    }

    @After
    public void tearDown() throws Exception {
        instance = null;
    }

    @Test
    public void testServerListCache() {
        Assert.assertNotNull(instance);

    }

    @Test
    public void testCreateEndpointTransportString() {
        Server server = instance.createEndpoint(transport, new CacheEndpoint("stringAddress", null));
        Assert.assertNotNull(server);
    }
}
