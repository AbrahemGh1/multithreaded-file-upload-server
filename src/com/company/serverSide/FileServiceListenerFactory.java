package com.company.serverSide;


public class FileServiceListenerFactory implements UploadFileServiceFactory {


  @Override
  public FileServiceListener createFileServiceListener(int portNumber) {
    return new FileServiceListener(portNumber);
  }
}
