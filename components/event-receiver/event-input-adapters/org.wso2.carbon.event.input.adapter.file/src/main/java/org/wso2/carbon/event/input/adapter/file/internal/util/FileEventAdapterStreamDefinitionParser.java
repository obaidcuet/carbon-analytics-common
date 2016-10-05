package org.wso2.carbon.event.input.adapter.file.internal.util;

/**
 * Created by hpadmin on 9/20/2016.
 */
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FileEventAdapterStreamDefinitionParser {

    public List<String> metaDataNames =  new ArrayList<String>();
    public List<String> correlationDataNames =  new ArrayList<String>();
    public List<String> payloadDataNames =  new ArrayList<String>();
    public int metaDataLength = 0;
    public int correlationDataLength = 0;
    public int payloadDataLength = 0;
    public int totalAttributes = 0;

    public FileEventAdapterStreamDefinitionParser (String jsonFilename) {
        readEventStreamsDef (jsonFilename);
    }

    public void readEventStreamsDef (String jsonFilename) {

        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(jsonFilename));

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray metaData = (JSONArray) jsonObject.get("metaData");
            JSONArray correlationData = (JSONArray) jsonObject.get("correlationData");
            JSONArray payloadData = (JSONArray) jsonObject.get("payloadData");

            if(metaData != null) {
                Iterator<JSONObject> metaDataIterator = metaData.iterator();
                List<String> metaDataNames =  new ArrayList<String>();
                while (metaDataIterator.hasNext()) {
                    metaDataNames.add((String)metaDataIterator.next().get("name"));
                }
                this.metaDataNames=metaDataNames;
                this.metaDataLength = this.metaDataNames.size();
            }

            if( correlationData != null) {
                Iterator<JSONObject> correlationDataIterator = correlationData.iterator();
                List<String> correlationDataNames =  new ArrayList<String>();
                while (correlationDataIterator.hasNext()) {
                    correlationDataNames.add((String) correlationDataIterator.next().get("name"));
                }
                this.correlationDataNames=correlationDataNames;
                this.correlationDataLength=this.correlationDataNames.size();
            }

            if( payloadData != null) {
                Iterator<JSONObject> payloadDataIterator = payloadData.iterator();
                List<String> payloadDataNames =  new ArrayList<String>();
                while (payloadDataIterator.hasNext()) {
                    payloadDataNames.add((String) payloadDataIterator.next().get("name"));
                }
                this.payloadDataNames=payloadDataNames;
                this.payloadDataLength=this.payloadDataNames.size();
            }

            this.totalAttributes = this.metaDataNames.size() + this.correlationDataNames.size() + this.payloadDataNames.size();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
