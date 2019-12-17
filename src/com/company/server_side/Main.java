package com.company.server_side;
import com.company.server_side.Service.Service;
public class Main{

  public static void main(String[] args) {
    // write your code here
    System.out.println("This from main");
    Service s= FileServiceAPI.createUploadFileService();
    s.startService();
  }

}




