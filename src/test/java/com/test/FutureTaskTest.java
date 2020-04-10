package com.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {
 @Test
    public void testFutureTask(){
     final String expectedResult = "Result from futuretask delayed 2 seconds";
     FutureTask<String> f = new FutureTask<>(new Callable<String>() {
         @Override
         public String call() throws Exception {
             Thread.sleep(2000);
             return expectedResult;
         }
     });
     new Thread(f).start();
     try{
         String result = f.get();
         Assert.assertEquals(expectedResult,result);
     }catch(InterruptedException e){
         e.printStackTrace();
     }catch(ExecutionException e){
         e.getCause().printStackTrace();
     }
 }
}
