package com.company.server_side.FileService;


import static com.company.server_side.ServerConfig.SERVER_PORT_NUMBER;

import com.company.server_side.any.ServiceStatus;

public class UploadFileService implements FileService {

  private FileServiceListener fileServiceListener;
  private Thread fileServiceListenerThread;


  public UploadFileService(UploadFileServiceFactory uploadFileServiceFactory) {
    fileServiceListener = uploadFileServiceFactory
        .createFileServiceListener(SERVER_PORT_NUMBER);
    fileServiceListenerThread = new Thread(fileServiceListener);
  }

  @Override
  public ServiceStatus getServiceStatus() {

    return fileServiceListener.getStatus();
  }


  @Override
  public void shutdownService() {
    fileServiceListener.stop();
  }


  @Override
  public void startService() {
    System.out.println("Upload File Service Now Listen To Port  2022");
    fileServiceListener.setStatus(ServiceStatus.SERVICE_UP);
    fileServiceListenerThread.start();

  }
}

