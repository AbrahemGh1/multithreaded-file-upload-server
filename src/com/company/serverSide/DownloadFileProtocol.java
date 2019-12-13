package com.company.serverSide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class DownloadFileProtocol {

  final boolean startDownload() {
    Path p = Paths.get(System.getProperty("user.dir"));
    String fileName = readFileName();
    if (!isFileExist(p, fileName)) {
      int fileSize = readFileSize();
      downloadFileContent(p, fileName, fileSize);
      return true;
    }
    return false;
  }

  protected abstract int readFileSize();

  public abstract String readFileName();

  public abstract boolean isFileExist(Path p, String fileName);

  public abstract void downloadFileContent(Path p, String fileName, int size);

}

class DownloadFileREQProtocol extends DownloadFileProtocol {

  private final Messenger MESSENGER;


  DownloadFileREQProtocol(SocketChannel clientSocket) {
    MESSENGER = new Messenger(clientSocket);
  }

  @Override
  public String readFileName() {
    MESSENGER.writeMessage(MessengerConstant.REQUEST_FILE_NAME);
    return MESSENGER.readMessage();
  }

  @Override
  protected int readFileSize() {
    MESSENGER.writeMessage(MessengerConstant.REQUEST_FILE_SIZE);
    return Integer.parseInt(MESSENGER.readMessage());
  }

  @Override
  public boolean isFileExist(Path p, String fileName) {
    return new File(p + File.separator + "ServerFiles" + File.separator + fileName).exists();
  }

  @Override
  public void downloadFileContent(Path p, String fileName, int fileSize) {
    MESSENGER.writeMessage(MessengerConstant.REQUEST_FILE_CONTENT);
    SocketChannel clientSocket = MESSENGER.getCLIENT_SOCKET();
    try (FileChannel fc = new FileOutputStream(fileName).getChannel()) {
      System.out.println(fc.transferFrom(clientSocket, 0, fileSize));
      System.out.println("File size: " + fc.size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

final class MessengerConstant {

   private  MessengerConstant() {
    throw new UnsupportedOperationException();
  }

  public static final String REQUEST_FILE_NAME = "REQUEST_FILE_NAME";
  public static final String REQUEST_FILE_SIZE = "REQUEST_FILE_SIZE";
  public static final String REQUEST_FILE_CONTENT = "REQUEST_FILE_CONTENT";


}
