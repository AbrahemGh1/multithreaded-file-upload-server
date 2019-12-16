package com.company.server_side.FileService;


public class StanderUploadFileService implements UploadFileServiceFactory {


  @Override
  public FileServiceListener createFileServiceListener(int portNumber) {
    return new StanderFileServiceListener(portNumber);
  }

}
