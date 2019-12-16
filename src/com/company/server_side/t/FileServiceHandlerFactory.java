package com.company.server_side.t;

import com.company.server_side.ReceiveFileProtocol;
import java.nio.channels.SocketChannel;

public interface FileServiceHandlerFactory {
  ReceiveFileProtocol createFileProtocol();
}
