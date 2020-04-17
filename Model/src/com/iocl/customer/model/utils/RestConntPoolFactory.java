package com.iocl.customer.model.utils;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;


public class RestConntPoolFactory <T> extends BasePooledObjectFactory<T> {
   private static Logger log = Logger.getLogger(RestConntPoolFactory.class);
    
    private HttpClient  httpClient=null;
    private String proxyServiceName;
    public RestConntPoolFactory(String serviceName) {
            super();
            proxyServiceName = serviceName;
        }

    @Override
    public T create() throws Exception {
        if(httpClient==null) {
            log.info("under in RestConnection==null"+httpClient);
            try {
               
                if(proxyServiceName!=null && proxyServiceName.equalsIgnoreCase("customerPool"))
                {
                RequestConfig requestConfig = RequestConfig.custom()
                                                           .setConnectTimeout(Integer.parseInt(ModelUtils.findKeyValue("CONNECTION_TIMEOUT")))
                                                           .setConnectionRequestTimeout(Integer.parseInt(ModelUtils.findKeyValue("CONNECTION_TIMEOUT")))
                                                           .setSocketTimeout(Integer.parseInt(ModelUtils.findKeyValue("CONNECTION_TIMEOUT")))
                                                           .build();
                httpClient = HttpClientBuilder.create()
                                              .setDefaultRequestConfig(requestConfig)
                                              .build();
                    return (T) httpClient;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error in RestConnection"+e);
            }
        }
        else {
            log.info("under in RestConnection!=null"+httpClient);
        }
          
        return null;
    }

    @Override
    public PooledObject<T> wrap(T restConnection) {
        return new DefaultPooledObject<>(restConnection);
    }
    
    @Override
        public PooledObject<T> makeObject() throws Exception {
            return wrap(create());
        }
}

