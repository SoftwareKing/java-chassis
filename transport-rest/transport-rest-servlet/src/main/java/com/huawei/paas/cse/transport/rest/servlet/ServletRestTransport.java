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

package com.huawei.paas.cse.transport.rest.servlet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.huawei.paas.cse.core.AsyncResponse;
import com.huawei.paas.cse.core.Const;
import com.huawei.paas.cse.core.Invocation;
import com.huawei.paas.cse.core.transport.AbstractTransport;
import com.huawei.paas.cse.transport.rest.client.RestTransportClient;
import com.huawei.paas.cse.transport.rest.client.RestTransportClientManager;
import com.huawei.paas.foundation.common.net.URIEndpointObject;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author   
 * @version  [版本号, 2017年1月2日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component
public class ServletRestTransport extends AbstractTransport {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return Const.RESTFUL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean init() throws Exception {
        String listenAddress = ServletConfig.getLocalServerAddress();
        if (!StringUtils.isEmpty(listenAddress)) {
            setListenAddressWithoutSchema(listenAddress);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Invocation invocation, AsyncResponse asyncResp) throws Exception {
        URIEndpointObject endpoint = (URIEndpointObject) invocation.getEndpoint().getAddress();
        RestTransportClient client =
            RestTransportClientManager.INSTANCE.getRestTransportClient(endpoint.isSslEnabled());
        client.send(invocation, asyncResp);
    }
}
