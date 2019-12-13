package com.company.serverSide;

import java.io.IOException;

public class Main{

  public static void main(String[] args) throws IOException {
    // write your code here
    Service service = new UploadFileService(new FileServiceListenerFactory());
    service.runService();
    System.out.println("This from main");
    System.out.println(service.getServiceStatus());



  }

}




