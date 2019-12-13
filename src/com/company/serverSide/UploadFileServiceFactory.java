package com.company.serverSide;

import com.company.serverSide.FileServiceListener;

public interface UploadFileServiceFactory {

  FileServiceListener createFileServiceListener(int portNumber);

}