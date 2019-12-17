package com.company.server_side.schedule;

import com.company.server_side.handler.ServiceHandler;

public class StanderUploadFileServiceScheduler extends UploadFileServiceScheduler {


  @Override
  public void schedule(ServiceHandler handler) {
    executor.submit(handler::serviceHandler);
  }
}
