package com.company.commonsUtility;

import java.io.IOException;
import java.nio.ByteBuffer;
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

  public void close() {
    try {
      CLIENT_SOCKET.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public SocketChannel getCLIENT_SOCKET() {
    return CLIENT_SOCKET;
  }
}
