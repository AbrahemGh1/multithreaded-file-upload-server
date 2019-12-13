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

  private int numberOfFiles;

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
      serverMessenger.writeMessage(MessengerConstant.START_UPLOAD_FILE);
      UploadFile((String) filesName.get(filesName.size() - 1));
      filesName.remove(filesName.size() - 1);
    }
  }

  private void pro() {
    String repo = "";
    serverMessenger.writeMessage(MessengerConstant.START_UPLOAD_FILE);

    repo = serverMessenger.readMessage();
    this.requestFileNameHandler();

    repo = serverMessenger.readMessage();
    if (repo.equals(MessengerConstant.FILE_EXIST_ON_SERVER)) {
      filesName.remove(filesName.size() - 1);
      if (filesName.isEmpty()) {
        this.requestNormalCloseHandler();
        serverMessenger.close();
      }
      pro();
    }
    this.requestFileSizeHandler();

    repo = serverMessenger.readMessage();
    this.requestFileContentHandler();

    repo = serverMessenger.readMessage();
    if (repo.equals(MessengerConstant.UPLOAD_FILES_FINISH)) {
      if (filesName.isEmpty()) {
        requestNormalCloseHandler();
      }
      filesName.remove(filesName.size() - 1);
      pro();
    }
  }

  private void UploadFile(String fileName) {
    String response = serverMessenger.readMessage();
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
  }

  private void requestFileNameHandler() {
    String fileName = (String) filesName.get(filesName.size() - 1);
    serverMessenger.writeMessage(fileName);
  }

  private void requestFileExistOnServerHandler() {
    serverMessenger.writeMessage(MessengerConstant.START_UPLOAD_FILE);
  }

  private void requestFileSizeHandler() {
    File f = new File(
        String.valueOf(directoryPath + File.separator + filesName.get(filesName.size() - 1)));
    try (FileInputStream fc2 = new FileInputStream(f)) {
      serverMessenger.writeMessage(fc2.getChannel().size() + "");
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private void requestFileContentHandler() {
    SocketChannel socketChannel = serverMessenger.getCLIENT_SOCKET();
    String fillPath = directoryPath + File.separator + filesName.get(filesName.size() - 1);
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
