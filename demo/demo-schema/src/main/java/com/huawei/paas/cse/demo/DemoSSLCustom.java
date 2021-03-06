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

package com.huawei.paas.cse.demo;

import java.io.File;
import java.io.IOException;

import com.huawei.paas.foundation.ssl.SSLCustom;

public class DemoSSLCustom extends SSLCustom {
    @Override
    public char[] decode(char[] encrypted) {
        return encrypted;
    }

    @Override
    public String getFullPath(String filename) {
        try {
            String currendDir = System.getProperty("user.dir");
            File cerDir = new File(currendDir, "src/main/resources/certificates");
            File certFile = new File(cerDir, filename);

            return certFile.getCanonicalPath();
        } catch (IOException e) {
            return filename;
        }
    }

}
