package com.iocl.customer.model.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

public class RestConnManager {
    private static GenericObjectPoolConfig config;
    private static Logger log = Logger.getLogger(RestConnManager.class);
        private static Map<String, RestConntPool> map = new HashMap<String, RestConntPool>();
    static {
            config = new GenericObjectPoolConfig();
            /**     Needs to shift these in the configuration file.*/
            config.setMaxTotal(50);
            config.setMaxIdle(25);
            config.setMinIdle(5);
            config.setMaxWaitMillis(100);
            config.setMinEvictableIdleTimeMillis(3600000);
            config.setTimeBetweenEvictionRunsMillis(1800000);
           // config.setTestOnBorrow(true);
           // config.setTestOnReturn(true);
           // config.setTestWhileIdle(true);
        }
    
    public <T> T  CreateConnection(String proxyService) {
        log.info("under  CreateConnection");
        RestConntPool<T> pool = null;
                synchronized (map) {
                    log.info("under Synchronized CreateConnection");
                    pool = map.get(proxyService);
                    if (pool == null) {
                        pool = new RestConntPool<T>(new RestConntPoolFactory<T>(proxyService), config);
                        map.put(proxyService, pool);
                    }
                }
        T httpRstClient = null;
        try {
            httpRstClient = pool.borrowObject();
            log.info("Pool Stats:" + proxyService + ": Created:[" + pool.getCreatedCount() + "], Active:[" +
                                    pool.getNumActive() + "], Borrowed:[" + pool.getBorrowedCount() + "]" + "], Idle:[" +
                                    pool.getNumIdle() + "]" + "], Waiters:[" + pool.getNumWaiters() + "]" + "], Returned:[" +
                                    pool.getReturnedCount() + "]" + "], Destroyed:[" + pool.getDestroyedCount() + "]");
                        
                            log.info("Pool Stats:" + proxyService + ": Created:[" + pool.getCreatedCount() + "], Active:[" +
                                    pool.getNumActive() + "], Borrowed:[" + pool.getBorrowedCount() + "]" + "], Idle:[" +
                                    pool.getNumIdle() + "]" + "], Waiters:[" + pool.getNumWaiters() + "]" + "], Returned:[" +
                                    pool.getReturnedCount() + "]" + "], Destroyed:[" + pool.getDestroyedCount() + "]");
        
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in Rest Connectiom"+e);
        }
        return httpRstClient;
    }
    
    
    public <T> void returnConnectionToPool(String proxyService, T jaxWsPort) {
           RestConntPool<T> pool = null;
           synchronized (map) {
               pool = map.get(proxyService);
           }
           try {
               if (pool == null) {
                   log.error("returnConnectionToPool - Invalid pool state" + proxyService + "]");
               }
               pool.returnObject(jaxWsPort);
           } catch (Exception e) {
               log.error("returnConnectionToPool - Exception Occured for" + proxyService + "Message: " +
                             e.getMessage() + "]");
           }
       }
   /* public static void main(String args[]) {
        RestConnManager obj=new RestConnManager();
        HttpClient httpClient=(HttpClient)obj.CreateConnection();
        System.out.println("d"+httpClient);
    }*/
    
  
}
