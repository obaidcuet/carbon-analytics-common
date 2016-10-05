package org.wso2.carbon.event.input.adapter.file;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.event.input.adapter.core.InputEventAdapterListener;

/**
 * Created by Md-Obaidul.Karim on 9/22/2016.
 */
public class eventProcessorThread implements Runnable {

    private String line;
    private InputEventAdapterListener eventAdapterListener;
    private int tenantId;

    public eventProcessorThread(InputEventAdapterListener eventAdapterListener, int tenantId, String line){
        this.line = line;
        this.eventAdapterListener = eventAdapterListener;
        this.tenantId = tenantId;
    }
    public void run() {
        sendLineEvent ();
    }

    public void sendLineEvent () {
        try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(this.tenantId);
            if (line.trim().length() > 0)
                eventAdapterListener.onEvent(this.line);
            PrivilegedCarbonContext.endTenantFlow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
