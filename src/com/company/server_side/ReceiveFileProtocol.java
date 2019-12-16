package com.company.server_side;


import static com.company.commonsUtility.MessengerConstant.FILE_EXIST_ON_SERVER;
import static com.company.commonsUtility.MessengerConstant.REQUEST_NORMAL_CLOSE;
import static com.company.commonsUtility.MessengerConstant.START_UPLOAD_FILE;
import static com.company.commonsUtility.MessengerConstant.UPLOAD_FILE_FINISH;

import com.company.commonsUtility.Messenger;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;


public abstract class ReceiveFileProtocol extends FileServiceHandler {

  protected final Messenger MESSENGER;

  public ReceiveFileProtocol(SocketChannel clientSocket) {
    super(10);
    MESSENGER = new Messenger(clientSocket);
  }

  public void startDownloadFiles() {
    String response = MESSENGER.readMessage();
    while (response.equals(START_UPLOAD_FILE)) {
      startDownloadFile();
      response = MESSENGER.readMessage();
      if (response.equals(REQUEST_NORMAL_CLOSE)) {
        break;
      }
    }
  }

  private final boolean startDownloadFile() {
    Path path = Paths.get(System.getProperty("user.dir"));
    String fileName = getFileNameFromClient();
    if (checkIfFileNameExist(path, fileName)) {
      MESSENGER.writeMessage(FILE_EXIST_ON_SERVER);
    } else {
      receiveFileContent(path, fileName, getFileSizeFromClient());
      MESSENGER.writeMessage(UPLOAD_FILE_FINISH);
    }
    return false;
  }

  protected abstract int getFileSizeFromClient();

  public abstract String getFileNameFromClient();

  public abstract boolean checkIfFileNameExist(Path path, String fileName);

  public abstract void receiveFileContent(Path path, String fileName, int size);

}

