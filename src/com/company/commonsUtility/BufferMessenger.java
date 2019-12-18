package com.company.commonsUtility;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * BufferMessenger class implement <@code>Messenger</code> and use <@code>ByteBuffer</code> to read
 * write message on <@code>SocketChannel</code>.
 */
public class BufferMessenger implements Messenger {

  private final SocketChannel CLIENT_SOCKET;
  private volatile boolean isOpen = true;

  public BufferMessenger(SocketChannel socketChannel) {

    this.CLIENT_SOCKET = socketChannel;
  }

  /**
   * Write String on SocketChannel .
   *
   * @param message to be written on messenger.
   * @throws NullPointerException if message null.
   */
  @Override
  public synchronized void writeMessage(@NotNull String message) {
    Objects.requireNonNull(message, "message cant be null");
    if (CLIENT_SOCKET.isOpen()) {
      try {
        CLIENT_SOCKET.write(ByteBuffer.wrap(message.getBytes()));
      } catch (IOException e) {

      }
    }
  }

  /**
   * Write String on SocketChannel .
   *
   * @return <@code>String</@code> message.
   */
  @Override
  public synchronized String readMessage() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(500);
    String message = "";
    try {
      CLIENT_SOCKET.read(byteBuffer);
      message = new String(byteBuffer.array(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      if (!isOpen) {
        System.out.println(MessengerConstant.ERROR_CLOSE);
      }
    }
    if (doesClientRequestsToClose(message)) {
      isOpen = true;
      this.close();
    }
    return message.trim();
  }

  private boolean doesClientRequestsToClose(String message) {
    return message.equals(MessengerConstant.REQUEST_NORMAL_CLOSE);
  }

  @Override
  public void close() {
    try {
      CLIENT_SOCKET.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public SocketChannel getClientSocket() {
    return CLIENT_SOCKET;
  }
}
