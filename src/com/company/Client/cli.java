package com.company.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import java.nio.file.Path;
import java.nio.file.Paths;

public class cli {
  private static String message="FileSize";
  public static void main(String[] args) throws IOException {
    SocketChannel socket = SocketChannel.open(new InetSocketAddress(
            "localhost", 2022));


    File f= new File("C:\\Users\\Abrahim\\Desktop\\untitled\\src\\Client1FilesToTest\\test0.txt");
    FileInputStream fc2 = new FileInputStream(f);
    FileChannel fc = fc2.getChannel();

    String response="";
    //Send file name
    response=readMessage(socket);
    System.out.println(response);
    message="REQUEST_FILE_NAME";
    if(response.equals(message)) {
      socket.write(ByteBuffer.wrap(f.getName().getBytes())); //9 bytes send name of file
    }

    //send file size
    response=readMessage(socket);
    System.out.println(response);
    message="REQUEST_FILE_SIZE";
    if(response.equals(message)) {
      String fr= fc.size()+"";
      socket.write(ByteBuffer.wrap(fr.getBytes())); //9 bytes send name of file
    }



    //send file contante
    response=readMessage(socket);
    message="REQUEST_FILE_CONTENT";
    System.out.println(response);
    if(response.equals(message)) {
      System.out.println(fc.transferTo(0, fc.size(), socket));  //send the file

      /*
      System.out.println("Sending file Size: " + fc.size());
      String fr= fc.size()+"";
      socket.write(ByteBuffer.wrap(fr.getBytes())); //9 bytes send name of file*/
    }

    System.out.println("Recarev from client:"+response);

    response = readMessage(socket);
    response = readMessage(socket);

    System.out.println(fc.size());
    fc.close();
   //CreateFile(5,"Client2FilesToTest");
  }
  static void CreateFile(int numberOfFiles ,String Dir ) throws IOException {
    System.out.println(System.getProperty("user.dir"));
    Path p2= Paths.get(System.getProperty("user.dir")+"//src//"+Dir);
    for(int i=0;i<numberOfFiles;i++) {
      File f = new File(String.valueOf(p2) + "//test"+i+".txt");
      DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(f));
      outputStream.writeBytes("This Text"+i+".txt for test");
      outputStream.close();
    }
  }

  private static String readMessage(SocketChannel socket) {
    ByteBuffer byteBuffer = ByteBuffer.allocate(500);
    String message = "";
    try {
      socket.read(byteBuffer);
      message = new String(byteBuffer.array(), "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return message.trim();
  }
}
