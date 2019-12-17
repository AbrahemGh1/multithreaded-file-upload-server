package com.company.server_side.protocols;


import static com.company.commonsUtility.MessengerConstant.FILE_EXIST_ON_SERVER;
import static com.company.commonsUtility.MessengerConstant.REQUEST_NORMAL_CLOSE;
import static com.company.commonsUtility.MessengerConstant.START_UPLOAD_FILE;
import static com.company.commonsUtility.MessengerConstant.UPLOAD_FILE_FINISH;

import com.company.commonsUtility.BufferMessenger;
import com.company.server_side.handler.ServiceHandler;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * ReceiveFileProtocol use Template method pattern to force all subclasses to follow the same
 * protocol for receive a file and left the other implementation for child class. Works as the
 * following protocol:
 *
 * <li>
 * <p>Receive file Name from client and check if file name exist on server</p>
 * <p>if the file exist on server: send to the client message indicate the file exist on server
 * and ask the client for next file name to be uploaded.</p>
 * </li>
 *
 * <li>
 * <p>Ask client for file Size from client.</p>
 * <p>Receive file Size from client.</p>
 * </li>
 *
 * <li>
 * <p>Ask client for file Size from client.</p>
 * <p>Receive file Content from client.</p>
 * </li>
 *
 * <li>
 * <p>send response message to client indicate that the file uploaded on server successfully.</p>
 * </li>
 *
 * @author Ibrahim Gharayibh
 * @see com.company.server_side.protocols.ReceiveFileTCPProtocol
 */

public abstract class ReceiveFileProtocol implements ServiceHandler {

  final BufferMessenger MESSENGER;

  ReceiveFileProtocol(SocketChannel clientSocket) {
    MESSENGER = new BufferMessenger(clientSocket);
  }

  /**
   * Keep download files until receive REQUEST_NORMAL_CLOSE message from client.
   */

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

  /**
   * Download a single file on server.
   *
   * @return <@code>true</code> if if file downloaded on server return <@code>false</code>
   * otherwise.
   */
  private boolean startDownloadFile() {
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

  /**
   * Read file name from client and return the name. More specific ask the
   * MESSENGER<@code>this</code> to request file name from client and read the next message from
   * client.
   *
   * @return an String value represents the file name.
   */
  public abstract String getFileNameFromClient();

  /**
   * Read file size from client and return the size. More specific ask the
   * MESSENGER<@code>this</code> to request file Size from client and read the next message from
   * client.
   *
   * @return an int value represents the file size.
   */
  protected abstract int getFileSizeFromClient();


  public abstract boolean checkIfFileNameExist(Path path, String fileName);

  /**
   * Receive file Content from client to store it to a specific path . More specific ask the
   * MESSENGER<@code>this</code> to request file Content from client and read file content from
   * client to store it in <@code>path</code>.
   *
   * @param path     <@code>Path</code> represent the path to save the content that will receive
   *                 from client.
   * @param fileName <@code>String</code> represent file name.
   * @param fileSize <@code>int</code> represent file size to be receive from client.
   */
  public abstract void receiveFileContent(Path path, String fileName, int fileSize);

  @Override
  public void serviceHandler() {
    startDownloadFiles();
  }

}

