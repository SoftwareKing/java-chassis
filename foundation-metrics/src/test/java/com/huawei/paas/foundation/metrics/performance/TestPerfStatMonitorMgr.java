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

package com.huawei.paas.foundation.metrics.performance;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author  
 * @since Mar 14, 2017
 * @see 
 */
public class TestPerfStatMonitorMgr {

    PerfStatMonitorMgr oPerfStatMonitorMgr = null;

    PerfStatSuccFail oPerfStatSuccFail = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        oPerfStatMonitorMgr = new PerfStatMonitorMgr();
        oPerfStatSuccFail = new PerfStatSuccFail("testMergeFrom");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        oPerfStatMonitorMgr = null;
        oPerfStatSuccFail = null;
    }

    /**
     * Test registerPerfStat
     */
    @Test
    public void testRegisterPerfStat() {
        oPerfStatMonitorMgr.registerPerfStat(oPerfStatSuccFail, 0);
        Assert.assertEquals(1, oPerfStatMonitorMgr.getMonitorList().size());
    }

    /**
     * Test onCycle
     */
    @Test
    public void testOnCycle() {
        oPerfStatMonitorMgr.registerPerfStat(oPerfStatSuccFail, 0);
        oPerfStatMonitorMgr.onCycle(System.currentTimeMillis(), 10);
        Assert.assertEquals(1,oPerfStatMonitorMgr.getMonitorPerfStat().size());

    }
}
