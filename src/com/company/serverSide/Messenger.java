package com.company.serverSide;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class Messenger {

  private final SocketChannel CLIENT_SOCKET;


  public Messenger(SocketChannel socketChannel) {

    this.CLIENT_SOCKET = socketChannel;
  }

  public void writeMessage(String message) {
    try {
      CLIENT_SOCKET.write(ByteBuffer.wrap(message.getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String readMessage() {

    ByteBuffer byteBuffer = ByteBuffer.allocate(500);
    String message = "";
    try {
      CLIENT_SOCKET.read(byteBuffer);
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

  private void download(String fileName, int fileSize) {
    try (FileChannel fc = new FileOutputStream(fileName).getChannel()) {

      System.out.println(fc.transferFrom(CLIENT_SOCKET, 0, fileSize));
      System.out.println("File size: " + fc.size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public SocketChannel getCLIENT_SOCKET(){
    return CLIENT_SOCKET;
  }
}
