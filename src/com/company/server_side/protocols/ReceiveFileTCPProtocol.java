package com.company.server_side.protocols;

import com.company.commonsUtility.MessengerConstant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class ReceiveFileTCPProtocol extends ReceiveFileProtocol {

  public ReceiveFileTCPProtocol(SocketChannel clientSocket) {
    super(clientSocket);
  }

  @Override
  public String getFileNameFromClient() {
    MESSENGER.writeMessage(MessengerConstant.REQUEST_FILE_NAME);
    return MESSENGER.readMessage();
  }

  @Override
  protected int getFileSizeFromClient() {
    MESSENGER.writeMessage(MessengerConstant.REQUEST_FILE_SIZE);
    return Integer.parseInt(MESSENGER.readMessage());
  }

  @Override
  public void receiveFileContent(Path path, String fileName, int fileSize) {
    MESSENGER.writeMessage(MessengerConstant.REQUEST_FILE_CONTENT);
    SocketChannel clientSocket = MESSENGER.getClientSocket();
    String filePath = path.toString() + File.separator + fileName;
    try (FileChannel fc = new FileOutputStream(filePath).getChannel()) {
      fc.transferFrom(clientSocket, 0, fileSize);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
