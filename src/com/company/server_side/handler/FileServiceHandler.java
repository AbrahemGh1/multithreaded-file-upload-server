package com.company.server_side.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class FileServiceHandler implements ServiceHandler {

  private final ExecutorService executor;

  public FileServiceHandler(int numberOfClient) {
    executor = Executors.newFixedThreadPool(numberOfClient);
  }

  public void schedule(UploaderFileServiceHandler handler) {
    executor.submit(handler::serviceHandler);
  }

}
