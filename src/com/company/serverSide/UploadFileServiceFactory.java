package com.company.serverSide;

interface UploadFileServiceFactory {

  FileServiceListener createFileServiceListener(int portNumber);

}