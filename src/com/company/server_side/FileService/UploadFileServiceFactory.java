package com.company.server_side.FileService;

interface UploadFileServiceFactory {

  /**
   * Create an Object of one of concentrate implantation of the FileServiceListener interface to run
   * on a specific port number.
   *
   * <p>
   *
   * @param portNumber on any port the listener will run.
   * @return FileServiceListener
   */
  FileServiceListener createFileServiceListener(int portNumber);
}
