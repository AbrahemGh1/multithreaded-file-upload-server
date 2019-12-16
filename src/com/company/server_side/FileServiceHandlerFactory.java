package com.company.server_side;

public interface FileServiceHandlerFactory {
  ReceiveFileProtocol createFileProtocol();
}
 class uploaderFileServiceHandler implements FileServiceHandlerFactory{


   @Override
   public ReceiveFileProtocol createFileProtocol() {
     return new ReceiveFileTCPProtocol(null);
   }
 }
