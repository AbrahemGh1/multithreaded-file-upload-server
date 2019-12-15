package com.company.serverSide;


public interface UploadFileServiceFactory {

  FileServiceListener createFileServiceListener(int portNumber);

}