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
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ClientTest {

  private static int portNumber = 2022;
  private final Messenger serverMessenger;
  private final Path directoryPath = Paths
      .get(System.getProperty("user.dir") + File.separator + "ClientFilesToTest");
  private List filesName;

  public ClientTest() throws IOException {
    SocketChannel socketChannel = SocketChannel
        .open(new InetSocketAddress("localhost", portNumber++));
    this.serverMessenger = new Messenger(socketChannel);
    filesName = getFilesNamesInDirectory();
  }

  private ArrayList getFilesNamesInDirectory() {
    List<String> FilesNamesInDirectory = null;
    try (Stream<Path> walk = Files.walk(directoryPath)) {
      FilesNamesInDirectory = walk.filter(Files::isRegularFile)
          .map(x -> x.toString()).filter(f -> f.endsWith(".txt")).collect(Collectors.toList());

      FilesNamesInDirectory.forEach(System.out::println);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return (ArrayList) FilesNamesInDirectory;
  }

  public void startUploadFiles() {
    while (!filesName.isEmpty()) {
      uploadFile();
      filesName.remove(filesName.size() - 1);
    }
  }

  private void uploadFile() {
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
          break;
        case MessengerConstant.REQUEST_NORMAL_CLOSE:
          requestNormalCloseHandler();
          break;
      }
      response = serverMessenger.readMessage();
    }
  }


  private void requestFileNameHandler() {
    String fileName = (String) filesName.get(filesName.size() - 1);
    System.out.println(Paths.get(fileName).getFileName().toFile());
    serverMessenger.writeMessage(String.valueOf(Paths.get(fileName).getFileName().toFile()));
  }

  private void requestFileExistOnServerHandler() {
    filesName.remove(filesName.size() - 1);
    if (filesName.isEmpty()) {
      serverMessenger.writeMessage(MessengerConstant.REQUEST_NORMAL_CLOSE);
    }
    serverMessenger.writeMessage(MessengerConstant.START_UPLOAD_FILE);
  }

  private void requestFileSizeHandler() {
    File f = new File((String) filesName.get(filesName.size() - 1));
    try (FileInputStream fc2 = new FileInputStream(f)) {
      serverMessenger.writeMessage(fc2.getChannel().size() + "");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void requestFileContentHandler() {
    SocketChannel socketChannel = serverMessenger.getCLIENT_SOCKET();
    String fillPath = (String) filesName.get(filesName.size() - 1);
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

}
