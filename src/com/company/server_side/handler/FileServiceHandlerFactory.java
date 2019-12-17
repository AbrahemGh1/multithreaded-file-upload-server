package com.company.server_side.handler;

import com.company.server_side.protocols.ReceiveFileProtocol;

public interface FileServiceHandlerFactory {
  ReceiveFileProtocol createFileProtocol();
}
