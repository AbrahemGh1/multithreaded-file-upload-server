package com.company.server_side;

import com.company.server_side.t.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class FileServiceHandler implements ServiceHandler {

  private final ExecutorService executor;

  public FileServiceHandler(int nThreads) {
    executor = Executors.newFixedThreadPool(nThreads);
  }

  public void schedule(UploaderFileServiceHandler handler) {
    executor.submit(handler::serviceHandler);
  }

}
