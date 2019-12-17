package com.company.server_side.schedule;

import com.company.server_side.handler.ServiceHandler;

public class StanderUploadFileServiceScheduler implements UploadFileServiceScheduler {


  @Override
  public void schedule(ServiceHandler handler) {
    handler.serviceHandler();

  }
}
