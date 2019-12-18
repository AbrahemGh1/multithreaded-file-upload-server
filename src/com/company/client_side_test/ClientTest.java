package com.company.client_side_test;

import static com.company.commonsUtility.MessengerConstant.FILE_EXIST_ON_SERVER;
import static com.company.commonsUtility.MessengerConstant.FILE_UPLOAD_SUCCESSFULLY;
import static com.company.commonsUtility.MessengerConstant.REQUEST_FILE_CONTENT;
import static com.company.commonsUtility.MessengerConstant.REQUEST_FILE_NAME;
import static com.company.commonsUtility.MessengerConstant.REQUEST_FILE_SIZE;
import static com.company.commonsUtility.MessengerConstant.REQUEST_NORMAL_CLOSE;
import static com.company.commonsUtility.MessengerConstant.START_UPLOAD_FILE;
import static com.company.commonsUtility.MessengerConstant.UPLOAD_FILE_FINISH;
import static com.company.server_side.Service.ServerConfig.SERVER_PORT_NUMBER;

import com.company.commonsUtility.BufferMessenger;
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

// this class only for test
class ClientTest extends Thread {

  static AtomicInteger numberOfAllClient = new AtomicInteger(0);
  private final int clientID;
  private final BufferMessenger serverMessenger;
  private final Path directoryPath =
      Paths.get(System.getProperty("user.dir") + File.separator + "ClientFilesToTest");
  private final CountDownLatch startLatch;
  private List filesPath;
  private int numberOfFilesUploadedByClient = 0;

  public ClientTest(CountDownLatch startLatch) throws IOException {
    synchronized (this) {
      clientID = numberOfAllClient.getAndAdd(1);
    }
    SocketChannel socketChannel;
    socketChannel = SocketChannel.open(new InetSocketAddress("localhost", SERVER_PORT_NUMBER));
    this.serverMessenger = new BufferMessenger(socketChannel);
    filesPath = getFilesPathsInDirectory();

    this.startLatch = startLatch;
  }

  @Override
  public void run() {
    try {
      startLatch.await(); // wait all threads to be in same point to start at same time
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.startUploadFiles();
    serverMessenger.close();
    System.out.println(this.toString());
    synchronized (this) {
      numberOfAllClient.getAndDecrement();
      if (numberOfAllClient.decrementAndGet() == 0) {
        System.exit(0);
      }
    }
  }

  private ArrayList getFilesPathsInDirectory() {
    List<String> filesNamesInDirectory = null;
    try (Stream<Path> walk = Files.walk(directoryPath)) {
      filesNamesInDirectory =
          walk.filter(Files::isRegularFile)
              .map(Path::toString)
              .filter(f -> f.endsWith(".txt"))
              .collect(Collectors.toList());
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
    serverMessenger.writeMessage(REQUEST_NORMAL_CLOSE);
    requestNormalCloseHandler();
  }

  private boolean uploadFile() {
    serverMessenger.writeMessage(START_UPLOAD_FILE);
    String response = serverMessenger.readMessage();
    while (!response.equals(UPLOAD_FILE_FINISH)) {
      switch (response) {
        case REQUEST_FILE_NAME:
          requestFileNameHandler();
          break;
        case REQUEST_FILE_SIZE:
          requestFileSizeHandler();
          break;
        case REQUEST_FILE_CONTENT:
          requestFileContentHandler();
          break;
        case FILE_EXIST_ON_SERVER:
          requestFileExistOnServerHandler();
          return false;
        case REQUEST_NORMAL_CLOSE:
          requestNormalCloseHandler();
          break;
      }
      response = serverMessenger.readMessage();
    }
    return true;
  }

  private void requestFileNameHandler() {
    String fileName = (String) filesPath.get(filesPath.size() - 1);
    serverMessenger.writeMessage(String.valueOf(Paths.get(fileName).getFileName().toFile()));
  }

  private void requestFileExistOnServerHandler() {
    System.out.println(
        "clientID="
            + clientID
            + "   "
            + Paths.get(filesPath.get(filesPath.size() - 1).toString()).toFile().getName()
            + "   "
            + FILE_EXIST_ON_SERVER);
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
      fc.transferTo(0, fc.size(), socketChannel);
      System.out.println(
          "clientID=" + clientID + "  " + f.getName() + "   " + FILE_UPLOAD_SUCCESSFULLY);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void requestNormalCloseHandler() {
    serverMessenger.close();
  }

  @Override
  public String toString() {
    return "ClientTest{"
        + "numberOfFilesUploadedByClient="
        + clientID
        + "   "
        + numberOfFilesUploadedByClient
        + '}';
  }
}
