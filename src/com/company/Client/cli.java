package com.company.Client;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

public class cli {


  public static void main(String[] args) throws IOException, InterruptedException {
    //create 100 clients each with try to upload same 100 files at the same time.

    createFiles(100,"ClientFilesToTest");
    int numberOfClientWantToTest = 100;
    ClientTest.clientID=numberOfClientWantToTest;
    CountDownLatch startLatch = new CountDownLatch(numberOfClientWantToTest);
    new ClientTest(startLatch);
    for (int threadNo = 0; threadNo < numberOfClientWantToTest; threadNo++) {
      Thread t = new ClientTest(startLatch);
      t.start();
    }
//// give the threads chance to start up
    startLatch.countDown();
  }

  static void createFiles(int numberOfFiles, String Dir) throws IOException {
    System.out.println(System.getProperty("user.dir"));
    File folder = new File(System.getProperty("user.dir") + File.separator + Dir);

    String dirPath = System.getProperty("user.dir") + Dir;
    if (Files.notExists(Paths.get(dirPath))) {
      folder.mkdir();
    }
    Path p2 = Paths.get(System.getProperty("user.dir") + File.separator + Dir);
    for (int i = 0; i < numberOfFiles; i++) {
      File f = new File(String.valueOf(p2) + "//test" + i + ".txt");
      DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(f));
      outputStream.writeBytes("This is Text" + i + ".txt for test");
      outputStream.close();
    }
  }
}

