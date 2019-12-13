package com.company.serverSide;

import com.company.serverSide.ReceiveFileProtocol;
import com.company.serverSide.ReceiveFileTCPProtocol;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;


public class UploaderFileServiceHandler{

  private final ReceiveFileProtocol receiveFileProtocol;


  public UploaderFileServiceHandler(@NotNull SocketChannel s) throws IOException {
    Objects.requireNonNull(s, "Socket must not be null.");
    receiveFileProtocol = new ReceiveFileTCPProtocol(s);
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
    receiveFileProtocol.startDownload();
  }
}
