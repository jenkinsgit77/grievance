package com.iocl.lpg.customer.bean.servo;

import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

public class FinOilFOrSectorBean {
    public FinOilFOrSectorBean() {
    }

    public String channelAction() {
        // Add event code here...
        //
        return null;
    }
    
    public String getKMBaseUrl(){
        String url=EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.KM_BROWSE_CHANNEL);
//        &cat=SELECTEDCATEGORY&channel=SELECTED/CLICKEDChannelImage
        return url;
    }
}
