package com.company.Client;


import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class cli {


  public static void main(String[] args) throws IOException, InterruptedException {
    //create 100 clients each with try to upload same 100 file at the same time.
    CountDownLatch startLatch = new CountDownLatch(1);


    startLatch.countDown();
    int numberOfClientWantToTest = 100;

    for (int threadNo = 0; threadNo < numberOfClientWantToTest; threadNo++) {
      Thread t = new ClientTest(startLatch);
      t.start();
    }
//// give the threads chance to start up
    startLatch.countDown();

  }
}

