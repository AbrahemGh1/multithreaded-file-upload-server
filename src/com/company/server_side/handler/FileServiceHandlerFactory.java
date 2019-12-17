package com.company.server_side.handler;

import com.company.server_side.protocol.ReceiveFileProtocol;

public interface FileServiceHandlerFactory {
  ReceiveFileProtocol createFileProtocol();
}
