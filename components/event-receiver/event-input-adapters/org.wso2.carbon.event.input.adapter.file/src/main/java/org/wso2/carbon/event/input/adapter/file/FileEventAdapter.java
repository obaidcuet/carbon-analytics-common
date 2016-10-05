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

import com.opencsv.CSVReader;
import org.json.simple.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.event.input.adapter.core.EventAdapterConstants;
import org.wso2.carbon.event.input.adapter.core.InputEventAdapter;
import org.wso2.carbon.event.input.adapter.core.InputEventAdapterConfiguration;
import org.wso2.carbon.event.input.adapter.core.InputEventAdapterListener;
import org.wso2.carbon.event.input.adapter.core.exception.InputEventAdapterException;
import org.wso2.carbon.event.input.adapter.core.exception.TestConnectionNotSupportedException;
import org.wso2.carbon.event.input.adapter.file.internal.util.FileEventAdapterConstants;
import org.wso2.carbon.event.input.adapter.file.internal.util.FileEventAdapterStreamDefinitionParser;



import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.nio.file.StandardCopyOption.*;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Input FileTailEventAdapter will be used to receive events from specified event file.
 */
public class FileEventAdapter implements InputEventAdapter {

    private final InputEventAdapterConfiguration eventAdapterConfiguration;
    private final Map<String, String> globalProperties;
    private InputEventAdapterListener eventAdapterListener;
    private final String id = UUID.randomUUID().toString();
    private static final Log log = LogFactory.getLog(FileEventAdapter.class);
    private ExecutorService singleThreadedExecutor;
    private Timer timer;
    private AtomicBoolean isThreadOccupied = new AtomicBoolean(false);
    private int tenantId;

    private String eventStreamDefFile;
    private String sourcePath;
    private String arcPath;
    private String filenameRegex = FileEventAdapterConstants.DEFAULT_FILENAME_REGEX;
    private int skipLine = FileEventAdapterConstants.DEFAULT_SKIPLINE;
    private char seperator = FileEventAdapterConstants.DEFAULT_SEPERATOR;
    private char quote = FileEventAdapterConstants.DEFAULT_QUOTE;
    private char escape = FileEventAdapterConstants.DEFAULT_ESCAPE;
    private int threads = FileEventAdapterConstants.DEFAULT_THREADS;
    private int batchSize = FileEventAdapterConstants.DEFAULT_BATCHSIZE;
    private int pullInterval = FileEventAdapterConstants.DEFAULT_PULL_INTERVAL;

    private FileEventAdapterStreamDefinitionParser eventStreamDef;


    public FileEventAdapter(InputEventAdapterConfiguration eventAdapterConfiguration,
                                Map<String, String> globalProperties) {
        this.eventAdapterConfiguration = eventAdapterConfiguration;
        this.globalProperties = globalProperties;
        this.singleThreadedExecutor = Executors.newSingleThreadExecutor();

        this.tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        this.timer = new Timer("PollTimer");

        loadFileAdapterConfig();
    }

    @Override
    public void init(InputEventAdapterListener eventAdapterListener) throws InputEventAdapterException {
        validateInputEventAdapterConfigurations();
        this.eventAdapterListener = eventAdapterListener;
    }

    @Override
    public void testConnect() throws TestConnectionNotSupportedException {
        throw new TestConnectionNotSupportedException("not-supported");
    }

    @Override
    public void connect() {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    pollForFile();
                } catch (Throwable e) {
                    log.error("Unexpected error when running polling task for file adapter.", e);
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, this.pullInterval * 1000, this.pullInterval * 1000);
    }

    @Override
    public void disconnect() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    @Override
    public void destroy() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileEventAdapter)) return false;

        FileEventAdapter that = (FileEventAdapter) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /* collecting configurations */
    private void loadFileAdapterConfig() {
        if(log.isDebugEnabled()){
            log.debug("New subscriber added for " + this.eventAdapterConfiguration.getName());
        }

        // mandatory parameters
        this.eventStreamDefFile = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_EVENT_STREAM_DEF);
        this.eventStreamDef = new FileEventAdapterStreamDefinitionParser(this.eventStreamDefFile);

        this.sourcePath = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SOURCEPATH);

        this.arcPath = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_ARCPATH);

        // optional parameters
        String filenameRegexProperty = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_FILENAME_REGEX);
        if (filenameRegexProperty != null && (!filenameRegexProperty.trim().isEmpty())) {
            this.filenameRegex = filenameRegexProperty;
        }

        String skipLineProperty = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SKIPLINE);
        if (skipLineProperty != null && (!skipLineProperty.trim().isEmpty())) {
            this.skipLine = Integer.parseInt(skipLineProperty);
        }

        String seperatorProperty = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_SEPERATOR);
        if (seperatorProperty != null && (!seperatorProperty.trim().isEmpty())) {
            this.seperator = seperatorProperty.trim().charAt(0);
        }

        String quoteProperty = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_QUOTE);
        if (quoteProperty != null && (!quoteProperty.trim().isEmpty())) {
            this.quote = quoteProperty.trim().charAt(0);
        }

        String escapeProperty = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_ESCAPE);
        if (escapeProperty != null && (!escapeProperty.trim().isEmpty())) {
            this.escape = escapeProperty.trim().charAt(0);
        }

        String threadsProperty = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_THREADS);
        if (threadsProperty != null && (!threadsProperty.trim().isEmpty())) {
            this.threads = Integer.parseInt(threadsProperty);
        }

        String batchSizeProperty = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_BATCHSIZE);
        if (batchSizeProperty != null && (!batchSizeProperty.trim().isEmpty())) {
            this.batchSize = Integer.parseInt(batchSizeProperty);
        }

        String pullIntervalProperty = this.eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_CONF_PULL_INTERVAL);
        if (pullIntervalProperty != null && (!pullIntervalProperty.trim().isEmpty())) {
            this.pullInterval = Integer.parseInt(pullIntervalProperty);
        }

    }

    /* main file processing implementation */
    private void pollForFile() {
        if (isThreadOccupied.compareAndSet(false, true)) {
            try {

                processFiles();

            } finally {
                isThreadOccupied.set(false);
            }
        }
    }

    public void processFiles() {
        try {
            // collect file in the source directory
            File folder = new File(this.sourcePath);
            File[] listOfFiles = folder.listFiles();
            //String patternString = ".*\\.csv$";

            for (int i = 0; i < listOfFiles.length; i++) {

                boolean isMatch = Pattern.matches(filenameRegex, listOfFiles[i].getName());
                if (isMatch) {
                    BufferedReader in = null;
                    ExecutorService executor = null;
                    try {
                        // initialize thread pool
                        executor = Executors.newFixedThreadPool(this.threads);

                        // loading file
                        in = new BufferedReader(new FileReader(listOfFiles[i].toPath().toString()));
                        String line=null;

                        // skip lines

                        int lineSkipped = 0;
                        while ( lineSkipped < this.skipLine && (line = in.readLine()) != null) {
                            lineSkipped = lineSkipped + 1;
                        }

                        // process line by line
                        int lineCount = 0;
                        String jsonArray = "";
                        line = null;
                        while ((line = in.readLine()) != null) {

                            lineCount = lineCount + 1;
                            jsonArray = jsonArray + formatLineToWSO2JSONEvent(line) + ",";

                            if (lineCount % this.batchSize == 0) {
                                executor.execute(new eventProcessorThread(this.eventAdapterListener, this.tenantId, "[" + jsonArray + "]"));
                                jsonArray = "";
                            }
                        }
                        executor.execute(new eventProcessorThread(this.eventAdapterListener, this.tenantId, "[" + jsonArray + "]"));

                        executor.shutdown();
                        // wait until all threads completes
                        while (!executor.isTerminated()) {
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // release resources
                        executor = null;
                        in.close();
                        in = null;
                        //System.gc();
                        // move current file to archive location
                        Files.move(listOfFiles[i].toPath(), new File(this.arcPath +"/"+ listOfFiles[i].getName()).toPath(), REPLACE_EXISTING);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // process string to appropriate WSO2 format (WSO2 JSON Event)
    public String formatLineToWSO2JSONEvent(String line) {

        CSVReader reader = new CSVReader(new StringReader(line), this.seperator, this.quote, this.escape, 0);
        String[] strEvent;

        JSONObject event = new JSONObject();
        JSONObject eventInner = new JSONObject();

        JSONObject metaData = new JSONObject();
        JSONObject correlationData = new JSONObject();
        JSONObject payloadData = new JSONObject();
        try {

            if ((strEvent = reader.readNext()) != null) {
                if(strEvent.length == this.eventStreamDef.totalAttributes) {
                    for (int i=0; i < strEvent.length; i++) {

                        if (i < this.eventStreamDef.metaDataLength && this.eventStreamDef.metaDataLength > 0)
                            metaData.put(this.eventStreamDef.metaDataNames.get(i),  strEvent[i]);

                        else if (i < this.eventStreamDef.metaDataLength + this.eventStreamDef.correlationDataLength && this.eventStreamDef.correlationDataLength > 0)
                            correlationData.put(this.eventStreamDef.correlationDataNames.get(i - this.eventStreamDef.metaDataLength),  strEvent[i]);

                        else if (i < this.eventStreamDef.metaDataLength + this.eventStreamDef.correlationDataLength + this.eventStreamDef.payloadDataLength && this.eventStreamDef.payloadDataLength > 0)
                            payloadData.put(this.eventStreamDef.payloadDataNames.get(i - this.eventStreamDef.metaDataLength - this.eventStreamDef.correlationDataLength),  strEvent[i]);
                    }

                } else {
                    reader.close();
                    return null;
                }

            }
            reader.close();

            eventInner.put("metaData", metaData);
            eventInner.put("correlationData", correlationData);
            eventInner.put("payloadData", payloadData);

            event.put("event", eventInner);

            return event.toJSONString();

        } catch(Exception ex) {
            ex.printStackTrace();
            return "";

        }
    }


    @Override
    public boolean isEventDuplicatedInCluster() {
        return Boolean.parseBoolean(globalProperties.get(EventAdapterConstants.EVENTS_DUPLICATED_IN_CLUSTER));
    }

    @Override
    public boolean isPolling() {
        return true;
    }

    /* need to dd some validations */
    private void validateInputEventAdapterConfigurations() throws InputEventAdapterException {

       /* String delayInMillisProperty = eventAdapterConfiguration.getProperties().get(FileEventAdapterConstants.EVENT_ADAPTER_DELAY_MILLIS);
        try{
            if(delayInMillisProperty != null){
                Integer.parseInt(delayInMillisProperty);
            }
        } catch (NumberFormatException e){
            throw new InputEventAdapterException("Invalid value set for property Delay: " + delayInMillisProperty, e);
        }*/
    }

}
