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

package com.huawei.paas.cse.transport.highway;

import com.huawei.paas.foundation.common.net.URIEndpointObject;
import com.huawei.paas.foundation.vertx.server.TcpServer;
import com.huawei.paas.foundation.vertx.server.TcpServerConnection;

public class HighwayServer extends TcpServer {
    /**
     * <构造函数>
     * @param endpointObject
     * @param tcpBufferHandler [参数说明]
     */
    public HighwayServer(URIEndpointObject endpointObject) {
        super(endpointObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TcpServerConnection createTcpServerConnection() {
        return new HighwayServerConnection();
    }
}
