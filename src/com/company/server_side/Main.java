package com.company.server_side;

import com.company.server_side.FileService.StanderUploadFileService;
import com.company.server_side.FileService.UploadFileService;
import com.company.server_side.Service.Service;
import java.io.IOException;

public class Main{

  public static void main(String[] args) throws IOException {
    // write your code here
    System.out.println("This from main");
    Service service = new UploadFileService(new StanderUploadFileService());
    service.startService();
    System.out.println(service.getServiceStatus());
    service.getServiceStatus();







  }

}




