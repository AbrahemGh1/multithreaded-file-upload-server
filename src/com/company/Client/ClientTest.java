package com.company.Client;

import com.company.commonsUtility.Messenger;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class ClientTest {

  private static int portNumber = 2022;

  private int numberOfFiles;

  final private Messenger serverMessenger;
  final Path p = Paths.get(System.getProperty("user.dir")+ File.separator+ "ClientFilesToTest");
  private final int NumberOfFilesInDirectory;

  public ClientTest() throws IOException {
    SocketChannel socketChannel = SocketChannel
        .open(new InetSocketAddress("localhost", portNumber++));
    this.serverMessenger = new Messenger(socketChannel);
    NumberOfFilesInDirectory=countNumberOfFileInDirectory(p);

  }

  public void setNumberOfFiles(int numberOfFiles) {
    this.numberOfFiles = numberOfFiles;
  }

 private int countNumberOfFileInDirectory(Path p){
   int count=0;
   try (Stream<Path> files = Files.list(Paths.get("your/path/here"))) {
      count = (int)files.count();
   } catch (IOException e) {
     e.printStackTrace();
   }
   return count;
 }

 private void startSendFiles(){
    int i=0;
    while (i<numberOfFiles){
      String fileName="";
      sendFile(fileName);
      i++;
    }
 }

  private void sendFile(String fileName) {


  }


}
