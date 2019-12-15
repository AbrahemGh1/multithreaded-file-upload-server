package com.company.serverSide;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileServiceListener implements Changeable {

  private static volatile Status fileServiceListenerStatus = Status.SERVICE_UP;
  private ServerSocketChannel serviceSocket;
  private int maxNumberOfClients = 10;
  private ExecutorService executor = Executors.newFixedThreadPool(maxNumberOfClients);


  FileServiceListener(int portNumber) {
    try {
      serviceSocket = ServerSocketChannel.open();
      serviceSocket.bind(new InetSocketAddress("localhost", portNumber));
    } catch (IOException e) {
      System.err.println(Arrays.toString(e.getStackTrace()));
    }
  }

  @Override
  public void run() {
    while (fileServiceListenerStatus == Status.SERVICE_UP) {
      try {
        System.out.println("Waiting for Connection:");
        SocketChannel clientSocket;
        synchronized (this) {
          clientSocket = serviceSocket.accept();
          System.out.println("Accept  Connection.");
          Thread t = new UploaderFileServiceHandler(clientSocket);
          executor.submit(t);

        }
      } catch (IOException e) {
        System.err.println(Arrays.toString(e.getStackTrace()));
      }
    }
  }

  @Override
  public void stop() {
    fileServiceListenerStatus = Status.SERVICE_DOWN;
  }

  public Status getStatus() {
    return fileServiceListenerStatus;
  }

  public void setStatus(Status status) {
    fileServiceListenerStatus = status;
  }
}
