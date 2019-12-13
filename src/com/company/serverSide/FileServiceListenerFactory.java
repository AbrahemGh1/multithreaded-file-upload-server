package com.company.serverSide;

class FileServiceListenerFactory implements UploadFileServiceFactory {


  @Override
  public FileServiceListener createFileServiceListener(int portNumber) {
    return new FileServiceListener(portNumber);
  }
}
