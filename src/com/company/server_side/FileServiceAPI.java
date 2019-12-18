package com.company.server_side;

import com.company.server_side.FileService.StanderUploadFileService;
import com.company.server_side.FileService.UploadFileService;

public class FileServiceAPI {

  private FileServiceAPI() {
    throw new UnsupportedOperationException();
  }

  public static UploadFileService createUploadFileService() {
    return new UploadFileService(new StanderUploadFileService());
  }
}
