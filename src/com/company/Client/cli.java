package com.company.Client;

import com.company.commonsUtility.Messenger;
import com.company.commonsUtility.MessengerConstant;
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

  private static String message = "FileSize";
  private static boolean connectionStatus = true;

  public static void main(String[] args) throws IOException {

    /*
     * ClientTest [] CT = new ClientTest[100];
     *
     * for(int i =0;i< 100;i++){
     * CT[i]= new ClientTest();
     * excitorservice.excute(CT);
     * }
     *
     *
     * */
    SocketChannel socket = SocketChannel.open(new InetSocketAddress(
        "localhost", 2022));

    File f = new File("C:\\Users\\Abrahim\\Desktop\\untitled\\src\\ClientFilesToTest\\test0.txt");
    FileInputStream fc2 = new FileInputStream(f);
    FileChannel fc = fc2.getChannel();

    String response = "";
    response = readMessage(socket);

    while (!response.equals(MessengerConstant.NORMAL_CLOSE)) {
      switch (response) {
        case MessengerConstant.REQUEST_FILE_NAME:
          socket.write(ByteBuffer.wrap(f.getName().getBytes()));
          requestFileNameHandler();
          break;
        case MessengerConstant.REQUEST_FILE_SIZE:
          String fr = fc.size() + "";
          socket.write(ByteBuffer.wrap(fr.getBytes())); //9 bytes send name of file
          requestFileSizeHandler();
          break;
        case MessengerConstant.REQUEST_FILE_CONTENT:
          System.out.println(fc.transferTo(0, fc.size(), socket));
          requestFileContentHandler();
          break;
        case MessengerConstant.FILE_EXIST_ON_SERVER:
          socket.write(ByteBuffer.wrap(MessengerConstant.NORMAL_CLOSE.getBytes()));
          requestFileExistOnServerHandler(socket);
          break;
        case MessengerConstant.ERROR_CLOSE:
          requestCloseHandler(socket);
          break;
      }
      response = readMessage(socket);
    }

    //CreateFile(5,"Client2FilesToTest");
  }

  private static void requestCloseHandler(SocketChannel socket) {
    if (connectionStatus) {
      try {
        socket.close();
        System.out.println(MessengerConstant.NORMAL_CLOSE);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private static void requestFileExistOnServerHandler(SocketChannel socket) {
    System.out.println(MessengerConstant.FILE_EXIST_ON_SERVER);
    connectionStatus = true;
    requestCloseHandler(socket);
  }

  private static void requestFileContentHandler() {

  }

  private static void requestFileSizeHandler() {
  }

  private static void requestFileNameHandler() {

  }

  static void CreateFile(int numberOfFiles, String Dir) throws IOException {
    System.out.println(System.getProperty("user.dir"));
    Path p2 = Paths.get(System.getProperty("user.dir") + "//src//" + Dir);
    for (int i = 0; i < numberOfFiles; i++) {
      File f = new File(String.valueOf(p2) + "//test" + i + ".txt");
      DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(f));
      outputStream.writeBytes("This Text" + i + ".txt for test");
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

