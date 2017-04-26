package com.mycompany.example.plugin.myhook

import org.junit.Test

/**
 * Purpose.
 *
 * A description of why this class exists.  
 *   For what reason was it written?  
 *   Which jobs does it perform?
 * {@code DataAccessException} using 
 * @author how
 * @date 17/4/17
 */
public  class MyRepositoryHookTest {



    @Test
    public void sendSearchEngine() throws Exception {
        MyRepositoryHook.sendSearchEngine("{\"a\":1}")
    }
}
