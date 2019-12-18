package com.company.server_side.FileService;

import com.company.server_side.Service.ServiceStatus;
import com.company.server_side.handler.ServiceHandler;
import com.company.server_side.handler.StanderUploaderFileServiceHandler;
import com.company.server_side.handler.UploaderFileServiceHandler;
import com.company.server_side.schedule.FileServiceScheduler;
import com.company.server_side.schedule.StanderUploadFileServiceScheduler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;

public class StanderFileServiceListener implements FileServiceListener {

  private volatile ServiceStatus fileServiceListenerStatus = ServiceStatus.SERVICE_UP;
  private ServerSocketChannel serviceSocket;
  private FileServiceScheduler standerUploadFileServiceScheduler =
      new StanderUploadFileServiceScheduler();

  protected StanderFileServiceListener(int portNumber) {

    try {
      serviceSocket = ServerSocketChannel.open();
      serviceSocket.bind(new InetSocketAddress("localhost", portNumber));
    } catch (IOException e) {
      System.err.println(Arrays.toString(e.getStackTrace()));
    }
  }

  @Override
  public void run() {
    while (fileServiceListenerStatus == ServiceStatus.SERVICE_UP) {
      try {
        System.out.println("Waiting for Connection from client:");
        handleRequest(serviceSocket.accept());
        System.out.println("Connection Accepted from client.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * handle client request by create UploaderFileServiceHandler and add it to schedule wait to be
   * start execute.
   *
   * @param clientSocket to be handle.
   * @throws IOException if any I/O Exception happen during communication
   */
  private void handleRequest(SocketChannel clientSocket) throws IOException {
    Objects.requireNonNull(clientSocket, "SocketChannel cant be null.");
    ServiceHandler h =
        new UploaderFileServiceHandler(
            new StanderUploaderFileServiceHandler(clientSocket)); // abstract factory pattern

    standerUploadFileServiceScheduler.schedule(h);
  }

  @Override
  public void stop() {
    fileServiceListenerStatus = ServiceStatus.SERVICE_DOWN;
  }

  @Override
  public ServiceStatus getStatus() {

    Objects.requireNonNull(fileServiceListenerStatus);
    return fileServiceListenerStatus;
  }

  @Override
  public void setStatus(ServiceStatus status) {
    fileServiceListenerStatus = status;
  }
}
