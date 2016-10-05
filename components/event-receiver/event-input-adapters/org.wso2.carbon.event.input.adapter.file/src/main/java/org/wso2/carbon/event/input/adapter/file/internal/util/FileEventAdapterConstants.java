/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Created by Md-Obaidul.Karim on 9/22/2016.
 */

package org.wso2.carbon.event.input.adapter.file.internal.util;


public final class FileEventAdapterConstants {

    private FileEventAdapterConstants() {
    }

    // mandatory parameters
    public static final String EVENT_ADAPTER_TYPE_FILE = "file";
    public static final String EVENT_ADAPTER_USAGE_TIPS_FILE = "file.usage.tips";

    public static final String EVENT_ADAPTER_CONF_EVENT_STREAM_DEF = "event.stream.def";
    public static final String EVENT_ADAPTER_CONF_EVENT_STREAM_DEF_HINT = "event.stream.def.hint";

    public static final String EVENT_ADAPTER_CONF_SOURCEPATH = "sourcepath";
    public static final String EVENT_ADAPTER_CONF_SOURCEPATH_HINT = "sourcepath.hint";

    public static final String EVENT_ADAPTER_CONF_ARCPATH = "arcpath";
    public static final String EVENT_ADAPTER_CONF_ARCPATH_HINT = "arcpath.hint";

    // optional parameters
    public static final String EVENT_ADAPTER_CONF_FILENAME_REGEX = "filename.regex";
    public static final String EVENT_ADAPTER_CONF_FILENAME_REGEX_HINT = "filename.regex.hint";

    public static final String EVENT_ADAPTER_CONF_SKIPLINE = "skipline";
    public static final String EVENT_ADAPTER_CONF_SKIPLINE_HINT = "skipline.hint";

    public static final String EVENT_ADAPTER_CONF_SEPERATOR = "seperator";
    public static final String EVENT_ADAPTER_CONF_SEPERATOR_HINT = "seperator.hint";

    public static final String EVENT_ADAPTER_CONF_QUOTE = "quote";
    public static final String EVENT_ADAPTER_CONF_QUOTE_HINT = "quote.hint";

    public static final String EVENT_ADAPTER_CONF_ESCAPE = "escape";
    public static final String EVENT_ADAPTER_CONF_ESCAPE_HINT = "escape.hint";

    public static final String EVENT_ADAPTER_CONF_THREADS = "threads";
    public static final String EVENT_ADAPTER_CONF_THREADS_HINT = "threads.hint";

    public static final String EVENT_ADAPTER_CONF_BATCHSIZE = "batchsize";
    public static final String EVENT_ADAPTER_CONF_BATCHSIZE_HINT = "batchsize.hint";

    public static final String EVENT_ADAPTER_CONF_PULL_INTERVAL = "pull.interval";
    public static final String EVENT_ADAPTER_CONF_PULL_INTERVAL_HINT = "pull.interval.hint";

    // default values for optional parameters
    public static final String DEFAULT_FILENAME_REGEX = ".*";
    public static final int DEFAULT_SKIPLINE = 0;
    public static final char DEFAULT_SEPERATOR =',';
    public static final char DEFAULT_QUOTE = '"';
    public static final char DEFAULT_ESCAPE = '\\';
    public static final int DEFAULT_THREADS = 1;
    public static final int DEFAULT_BATCHSIZE = 100;
    public static final int DEFAULT_PULL_INTERVAL = 1;

}
