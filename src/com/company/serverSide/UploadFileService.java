package com.company.serverSide;

class UploadFileService implements FileService {

  private FileServiceListener fileServiceListener;
  private Thread fileServiceListenerThread;

  UploadFileService(UploadFileServiceFactory uploadFileServiceFactory) {
    fileServiceListener = uploadFileServiceFactory.createFileServiceListener(2022);//need to change
    fileServiceListenerThread = new Thread(fileServiceListener);
  }

  @Override
  public void shutdownService() {
    fileServiceListener.stop();
  }

  @Override
  public Status getServiceStatus() {

    return fileServiceListener.getStatus();
  }

  @Override
  public void runService() {
    System.out.println("Upload File Service Now Listen To Port  2022");
    fileServiceListener.setStatus(Status.SERVICE_UP);
    fileServiceListenerThread.start();

  }
}

