package com.company.serverSide;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;


class UploaderFileServiceHandler extends Thread{

  private final SocketChannel clientSocket;
  private final DownloadFileProtocol DownloadFileREQProtocol1;

  /**
   * 152      * Set the source for environment variables. 153      * 154      * @implSpec If this
   * method is not called, the behavior should be 155      * equivalent to calling {@code
   * env(System.getenv())}. 156      * 157      * @param vars the Map of environment variable names
   * to values 158      * @return the {@code JavaShellToolBuilder} instance 159
   */

  UploaderFileServiceHandler(@NotNull SocketChannel s) throws IOException {
    Objects.requireNonNull(s, "Socket must not be null.");
    clientSocket = s;
    DownloadFileREQProtocol1= new DownloadFileREQProtocol(s);
    // clientSocket.configureBlocking(false);
    //FileChannel fc = new FileOutputStream("test3").getChannel();
  }

  public static void closeConnection(@NotNull Socket socket) {
    Objects.requireNonNull(socket, "Socket must not be null.");
    try {
      socket.close();
    } catch (IOException e) {
      System.err.println(Arrays.toString(e.getStackTrace()));
    }
  }

  public void s() {
    DownloadFileREQProtocol1.startDownload();
    String fileName = readMessage();
    System.out.println("fileName: " + fileName);
    writeMessage("FileSize");
    int fileSize = Integer.parseInt(readMessage());
    System.out.println("FileSize: " + fileSize);
    writeMessage("Server Recirv File Size" + fileSize);
    System.out.println("Download  :" + fileName + "On server");
    download(fileName, fileSize);
  }

  private void download(String fileName, int fileSize) {
    try (FileChannel fc = new FileOutputStream(fileName).getChannel()) {

      System.out.println(fc.transferFrom(clientSocket, 0, fileSize));
      System.out.println("File size: " + fc.size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String readMessage() {

    ByteBuffer byteBuffer = ByteBuffer.allocate(500);
    String message = "";
    try {
      clientSocket.read(byteBuffer);
      message = new String(byteBuffer.array(), "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (message.trim().equals("")) {
      System.out.println("Nothing");
      return "Nothing";
    }
    System.out.println(message);
    return message.trim();
  }

  private void writeMessage(String message) {
    try {
      clientSocket.write(ByteBuffer.wrap(message.getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
