package com.company.server_side.schedule;

import com.company.server_side.handler.ServiceHandler;

public interface ServiceScheduler {
  void schedule(ServiceHandler handler);
}
