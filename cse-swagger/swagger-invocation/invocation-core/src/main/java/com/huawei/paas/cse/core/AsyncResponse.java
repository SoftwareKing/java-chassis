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

package com.huawei.paas.cse.core;

import javax.ws.rs.core.Response.StatusType;

import com.huawei.paas.cse.core.invocation.InvocationType;

public interface AsyncResponse {
    /**
     * Something has happened, so handle it.
     *
     * @param event  the event to handle
     */
    void handle(Response response);

    default void success(StatusType status, Object result) {
        handle(Response.status(status).entity(result));
    }

    default void success(Object result) {
        handle(Response.ok(result));
    }

    default void consumerFail(Throwable e) {
        handle(Response.createConsumerFail(e));
    }

    default void producerFail(Throwable e) {
        handle(Response.createProducerFail(e));
    }

    default void fail(InvocationType type, Throwable e) {
        handle(Response.createFail(type, e));
    }

    default void complete(Response resp) {
        handle(resp);
    }
}
