package com.company.server_side;


import static com.company.server_side.ServerConfig.MAX_NUMBER_CLIENTS;

import com.sun.istack.internal.NotNull;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;



public class UploaderFileServiceHandler extends FileServiceHandler {

  private final ReceiveFileProtocol receiveFileProtocol;


  public UploaderFileServiceHandler(@NotNull SocketChannel s) {
    super(MAX_NUMBER_CLIENTS);
    Objects.requireNonNull(s, "The SocketChannel parameter must not be null.");
    receiveFileProtocol = new ReceiveFileTCPProtocol(s);
  }

  public static void closeConnection(@NotNull Socket socket) {
    Objects.requireNonNull(socket, " The Socket parameter must not be null.");
    try {
      socket.close();
    } catch (IOException e) {
      System.err.println(Arrays.toString(e.getStackTrace()));
    }
  }

  @Override
  public void serviceHandler() {
    receiveFileProtocol.startDownloadFiles();

  }
}