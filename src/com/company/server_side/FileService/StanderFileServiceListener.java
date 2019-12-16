package com.company.server_side.FileService;

import com.company.server_side.t.*;
import com.company.server_side.any.ServiceStatus;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;


public class StanderFileServiceListener implements FileServiceListener {

  private volatile ServiceStatus fileServiceListenerStatus = ServiceStatus.SERVICE_UP;
  private ServerSocketChannel serviceSocket;

  StanderFileServiceListener(int portNumber) {
    try {
      serviceSocket = ServerSocketChannel.open();
      serviceSocket.bind(new InetSocketAddress("localhost", portNumber));//change
    } catch (IOException e) {
      System.err.println(Arrays.toString(e.getStackTrace()));

    }
  }

  @Override
  public void run() {
    System.out.println("Waiting for Connection from client:");
    while (fileServiceListenerStatus == ServiceStatus.SERVICE_UP) {
      try {
        handleRequest(serviceSocket.accept());
        System.out.println("Connection Accepted from client.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @param clientSocket to be handle
   *                     <p>handle client request by create UploaderFileServiceHandler
   *                     and add it to schedule wait to be start execute
   *                     <p/>
   * @throws IOException if any I/O Exception happen during communication
   */
  private synchronized void handleRequest(SocketChannel clientSocket) throws IOException {
    Objects.requireNonNull(clientSocket, "SocketChannel cant be null.");
    UploaderFileServiceHandler service = new UploaderFileServiceHandler(new StanderUploaderFileServiceHandler(clientSocket));
    service.schedule((service));
  }

  @Override
  public void stop() {
    fileServiceListenerStatus = ServiceStatus.SERVICE_DOWN;
  }

  @Override
  public ServiceStatus getStatus() {
    return fileServiceListenerStatus;
  }

  @Override
  public void setStatus(ServiceStatus status) {
    fileServiceListenerStatus = status;
  }
}
