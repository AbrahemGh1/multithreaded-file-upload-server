package com.company.server_side.t;

import com.company.server_side.ReceiveFileProtocol;
import com.company.server_side.ReceiveFileTCPProtocol;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class StanderUploaderFileServiceHandler implements FileServiceHandlerFactory{
  private final SocketChannel s;

  public StanderUploaderFileServiceHandler(SocketChannel s) {
    this.s = s;
  }

  @Override
  public ReceiveFileProtocol createFileProtocol() {
    Objects.requireNonNull(s);
    return new ReceiveFileTCPProtocol(s);
  }
}
