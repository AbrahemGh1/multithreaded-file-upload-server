package com.company.server_side.schedule;

import com.company.server_side.handler.ServiceHandler;

public interface FileServiceScheduler {

  void schedule(ServiceHandler handler);
}
