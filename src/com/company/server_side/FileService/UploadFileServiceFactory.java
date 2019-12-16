package com.company.server_side.FileService;


 interface UploadFileServiceFactory {

  /**
   * <p>Create a Object of one of concentrate implantation of the FileServiceListener interface to run
   * on a specific port number.<p/>
   * More specific return StanderFileServiceListener
   *
   * @param portNumber on any port the listener will run
   * @return FileServiceListener
   */
  FileServiceListener createFileServiceListener(int portNumber);
}