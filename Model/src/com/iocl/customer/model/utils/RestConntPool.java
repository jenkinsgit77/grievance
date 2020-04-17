package com.iocl.customer.model.utils;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
public class RestConntPool <Q> extends GenericObjectPool<Q> {
    /**
         * Constructor.
         * 
         * It uses the default configuration for pool provided by
         * apache-commons-pool2.
         * 
         * @param factory
         */
    public RestConntPool(PooledObjectFactory<Q> factory) {
        super(factory);
    }
    /**
        * Constructor.
        * 
        * This can be used to have full control over the pool using configuration
        * object.
        * 
        * @param factory
        * @param config
        */
    public RestConntPool(PooledObjectFactory<Q> factory,
               GenericObjectPoolConfig config) {
           super(factory, config);
       }
}