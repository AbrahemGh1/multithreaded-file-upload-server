package com.company.Client;


import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class cli {


  public static void main(String[] args) throws IOException, InterruptedException {
    //create 100 clients each with try to upload same 100 files at the same time.


    int numberOfClientWantToTest = 10;
    ClientTest.numberOfClient=10;
    CountDownLatch startLatch = new CountDownLatch(numberOfClientWantToTest);

    for (int threadNo = 0; threadNo < numberOfClientWantToTest; threadNo++) {
      Thread t = new ClientTest(startLatch);
      t.start();
    }
//// give the threads chance to start up
    startLatch.countDown();



  }
}

