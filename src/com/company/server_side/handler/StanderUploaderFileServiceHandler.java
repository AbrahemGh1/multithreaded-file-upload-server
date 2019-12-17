package com.company.server_side.handler;

import com.company.server_side.protocols.ReceiveFileProtocol;
import com.company.server_side.protocols.ReceiveFileTCPProtocol;
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
