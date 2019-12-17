package com.company.Client;

import com.company.commonsUtility.Messenger;
import com.company.commonsUtility.MessengerConstant;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ClientTest extends Thread {

  private static Integer portNumber = 2022;
  public static volatile int numberOfClient = 0;
  private final Messenger serverMessenger;
  private final Path directoryPath = Paths
      .get(System.getProperty("user.dir") + File.separator + "ClientFilesToTest");
  private final CountDownLatch startLatch;
  private List filesPath;
  private int numberOfFilesUploadedByClient = 0;


  public ClientTest(CountDownLatch startLatch) throws IOException {
    SocketChannel socketChannel;
      System.out.println();
      socketChannel = SocketChannel
          .open(new InetSocketAddress("localhost", portNumber));
    this.serverMessenger = new Messenger(socketChannel);
    filesPath = getFilesNamesInDirectory();

    this.startLatch = startLatch;
    startLatch.countDown();
  }

  @Override
  public void run() {
//    try {
//      startLatch.await();// wait all threads to be in same point to start at same time
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
      this.startUploadFiles();
    System.out.println("numberOfClient: "+numberOfClient--);
    serverMessenger.close();
    this.toString();
    if(numberOfClient==0)
      System.exit(0);
  }

  private ArrayList getFilesNamesInDirectory() {
    List<String> filesNamesInDirectory = null;
    try (Stream<Path> walk = Files.walk(directoryPath)) {
      filesNamesInDirectory = walk.filter(Files::isRegularFile)
          .map(Path::toString).filter(f -> f.endsWith(".txt")).collect(Collectors.toList());

      filesNamesInDirectory.forEach(System.out::println);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return (ArrayList) filesNamesInDirectory;
  }

  public void startUploadFiles() {
    while (!filesPath.isEmpty()) {
      if (uploadFile()) {
        numberOfFilesUploadedByClient++;
      }
      filesPath.remove(filesPath.size() - 1);
    }
    this.toString();
    serverMessenger.writeMessage(MessengerConstant.REQUEST_NORMAL_CLOSE);
    requestNormalCloseHandler();
  }

  private boolean uploadFile() {
    serverMessenger.writeMessage(MessengerConstant.START_UPLOAD_FILE);
    String response = serverMessenger.readMessage();
    while (!response.equals(MessengerConstant.UPLOAD_FILE_FINISH)) {
      switch (response) {
        case MessengerConstant.REQUEST_FILE_NAME:
          requestFileNameHandler();
          break;
        case MessengerConstant.REQUEST_FILE_SIZE:
          requestFileSizeHandler();
          break;
        case MessengerConstant.REQUEST_FILE_CONTENT:
          requestFileContentHandler();
          break;
        case MessengerConstant.FILE_EXIST_ON_SERVER:
          requestFileExistOnServerHandler();
          return false;
        case MessengerConstant.REQUEST_NORMAL_CLOSE:
          requestNormalCloseHandler();
          break;
      }
      response = serverMessenger.readMessage();
    }
    return true;
  }


  private void requestFileNameHandler() {
    String fileName = (String) filesPath.get(filesPath.size() - 1);
    System.out.println(Paths.get(fileName).getFileName().toFile());
    serverMessenger.writeMessage(String.valueOf(Paths.get(fileName).getFileName().toFile()));
  }

  private void requestFileExistOnServerHandler() {
    System.out.println(
       Paths.get(filesPath.get(filesPath.size() - 1).toString()).toFile().getName()  + ": " + MessengerConstant.FILE_EXIST_ON_SERVER);
  }

  private void requestFileSizeHandler() {
    File f = new File((String) filesPath.get(filesPath.size() - 1));
    try (FileInputStream fc2 = new FileInputStream(f)) {
      serverMessenger.writeMessage(fc2.getChannel().size() + "");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void requestFileContentHandler() {
    SocketChannel socketChannel = serverMessenger.getClientSocket();
    String fillPath = (String) filesPath.get(filesPath.size() - 1);
    try {
      File f = new File(fillPath);
      FileInputStream fileInputStream = new FileInputStream(f);
      FileChannel fc = fileInputStream.getChannel();
      System.out.println(fc.transferTo(0, fc.size(), socketChannel));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void requestNormalCloseHandler() {
    serverMessenger.close();
  }

  @Override
  public String toString() {
    return "ClientTest{" +"ClientTest.numberOfClient="+ClientTest.numberOfClient+
        "numberOfFilesUploadedByClient=" + numberOfFilesUploadedByClient +
        '}';
  }
}
