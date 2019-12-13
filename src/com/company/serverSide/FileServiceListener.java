package com.company.serverSide;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileServiceListener implements Changeable {

  private int maxNumberOfClients=100;
  private ServerSocketChannel serviceSocket;
  private static volatile Status FileServiceListenerStatus = Status.SERVICE_UP;
  private ExecutorService executor = Executors.newFixedThreadPool(maxNumberOfClients);


  public void setStatus(Status status) {
    this.FileServiceListenerStatus = status;
  }

  FileServiceListener(int portNumber) {
    try {
      serviceSocket= ServerSocketChannel.open();
      serviceSocket.bind(new InetSocketAddress("localhost", portNumber));
    } catch (IOException e) {
      System.err.println(Arrays.toString(e.getStackTrace()));
    }
  }

  @Override
  public void run() {
    while (FileServiceListenerStatus == Status.SERVICE_UP) {
      try {
        SocketChannel clientSocket;
        System.out.println("Waiting for Connection:");
        synchronized (this){
          clientSocket=serviceSocket.accept();
          System.out.println("Accept  Connection.");
          new UploaderFileServiceHandler(clientSocket).s();
        }
      } catch (IOException e) {
        System.err.println(Arrays.toString(e.getStackTrace()));
      }
    }
  }

  @Override
  public void stop() {
    FileServiceListenerStatus = Status.SERVICE_DOWN;
  }

  public Status getStatus() {
    return FileServiceListenerStatus;
  }
}
