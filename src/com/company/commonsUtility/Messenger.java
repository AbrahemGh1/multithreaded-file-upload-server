package com.company.commonsUtility;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class Messenger {

  private final SocketChannel CLIENT_SOCKET;


  public Messenger(SocketChannel socketChannel) {

    this.CLIENT_SOCKET = socketChannel;
  }

  public void writeMessage(@NotNull String message) {
    Objects.requireNonNull(message, "message cant be null");
    if (CLIENT_SOCKET.isOpen()) {
      try {
        CLIENT_SOCKET.write(ByteBuffer.wrap(message.getBytes()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String readMessage() {

    ByteBuffer byteBuffer = ByteBuffer.allocate(500);
    String message = "";
    try {
      CLIENT_SOCKET.read(byteBuffer);
      message = new String(byteBuffer.array(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.out.println(MessengerConstant.ERROR_CLOSE);
    }
    if (message.trim().equals("")) {
      System.out.println("Nothing");
      return "Nothing";
    }
    System.out.println(message);
    if(doesClientRequestsToClose(message))
      this.close();
    return message.trim();
  }
  private boolean doesClientRequestsToClose(String message){
    return message.equals(MessengerConstant.REQUEST_NORMAL_CLOSE);
  }
  public void close(){
    try {
      CLIENT_SOCKET.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public SocketChannel getClientSocket(){
    return CLIENT_SOCKET;
  }
}
