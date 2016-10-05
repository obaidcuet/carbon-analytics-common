
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

package org.wso2.carbon.event.input.adapter.file;

import org.wso2.carbon.event.input.adapter.core.*;
import org.wso2.carbon.event.input.adapter.file.internal.util.FileEventAdapterConstants;

import java.util.*;

/**
 * The file event adapter factory class to create a file input adapter
 */
public class FileEventAdapterFactory extends InputEventAdapterFactory {

    private ResourceBundle resourceBundle = ResourceBundle.getBundle
            ("org.wso2.carbon.event.input.adapter.file.i18n.Resources", Locale.getDefault());

    @Override
    public String getType() {
        return FileEventAdapterConstants.EVENT_ADAPTER_TYPE_FILE;
    }

    @Override
    public List<String> getSupportedMessageFormats() {
        List<String> supportInputMessageTypes = new ArrayList<String>();
        // just converting the type to string value to avoid error "Text Mapping is not supported by event adapter type file"
        String jsonType = MessageType.JSON;
        supportInputMessageTypes.add(jsonType);
        //supportInputMessageTypes.add(MessageType.JSON);
        //supportInputMessageTypes.add(MessageType.XML);
        //supportInputMessageTypes.add(MessageType.TEXT);
        return supportInputMessageTypes;
    }

    @Override
    public List<Property> getPropertyList() {
        List<Property> propertyList = new ArrayList<Property>();

        Property eventStreamDef = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_EVENT_STREAM_DEF);
        eventStreamDef.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_EVENT_STREAM_DEF));
        eventStreamDef.setRequired(true);
        eventStreamDef.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_EVENT_STREAM_DEF_HINT));
        propertyList.add(eventStreamDef);

        Property sourcepath = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SOURCEPATH);
        sourcepath.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SOURCEPATH));
        sourcepath.setRequired(true);
        sourcepath.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SOURCEPATH_HINT));
        propertyList.add(sourcepath);

        Property arcpath = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_ARCPATH);
        arcpath.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_ARCPATH));
        arcpath.setRequired(true);
        arcpath.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_ARCPATH_HINT));
        propertyList.add(arcpath);

        Property filenameRegex = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_FILENAME_REGEX);
        filenameRegex.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_FILENAME_REGEX));
        //filenameRegex.setDefaultValue(FileEventAdapterConstants.DEFAULT_FILENAME_REGEX);
        filenameRegex.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_FILENAME_REGEX_HINT));
        propertyList.add(filenameRegex);

        Property skipline = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SKIPLINE);
        skipline.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SKIPLINE));
        //skipline.setDefaultValue(FileEventAdapterConstants.DEFAULT_SKIPLINE+"");
        skipline.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SKIPLINE_HINT));
        propertyList.add(skipline);

        Property seperator = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SEPERATOR);
        seperator.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SEPERATOR));
        //seperator.setDefaultValue(FileEventAdapterConstants.DEFAULT_SEPERATOR+"");
        seperator.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SEPERATOR_HINT));
        propertyList.add(seperator);

        Property quote = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_QUOTE);
        quote.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_QUOTE));
        //quote.setDefaultValue(FileEventAdapterConstants.DEFAULT_QUOTE+"");
        quote.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_QUOTE_HINT));
        propertyList.add(quote);

        Property escape = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_ESCAPE);
        escape.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_ESCAPE));
        //escape.setDefaultValue(FileEventAdapterConstants.DEFAULT_ESCAPE+"");
        escape.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_ESCAPE_HINT));
        propertyList.add(escape);

        Property threads = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_THREADS);
        threads.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_THREADS));
        //threads.setDefaultValue(FileEventAdapterConstants.DEFAULT_THREADS+"");
        threads.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_THREADS_HINT));
        propertyList.add(threads);

        Property batchsize = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_BATCHSIZE);
        batchsize.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_BATCHSIZE));
        //batchsize.setDefaultValue(FileEventAdapterConstants.DEFAULT_BATCHSIZE+"");
        batchsize.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_BATCHSIZE_HINT));
        propertyList.add(batchsize);

        Property pullInterval = new Property(FileEventAdapterConstants.EVENT_ADAPTER_CONF_PULL_INTERVAL);
        pullInterval.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_PULL_INTERVAL));
        //pullInterval.setDefaultValue(FileEventAdapterConstants.DEFAULT_PULL_INTERVAL+"");
        pullInterval.setHint(resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_CONF_PULL_INTERVAL_HINT));
        propertyList.add(pullInterval);



        /*Property startFromEndProperty = new Property(FileEventAdapterConstants.EVENT_ADAPTER_START_FROM_END);
        startFromEndProperty.setRequired(true);
        startFromEndProperty.setDisplayName(
                resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_START_FROM_END));
        startFromEndProperty.setOptions(new String[]{"true", "false"});
        startFromEndProperty.setDefaultValue("true");
        startFromEndProperty.setHint(resourceBundle.getString(
                FileEventAdapterConstants.EVENT_ADAPTER_START_FROM_END_HINT));
        propertyList.add(startFromEndProperty);*/

        return propertyList;
    }

    @Override
    public String getUsageTips() {
        return resourceBundle.getString(FileEventAdapterConstants.EVENT_ADAPTER_USAGE_TIPS_FILE);
    }

    @Override
    public InputEventAdapter createEventAdapter(InputEventAdapterConfiguration eventAdapterConfiguration,
                                                Map<String, String> globalProperties) {
        return new FileEventAdapter(eventAdapterConfiguration, globalProperties);
    }
}
