package com.company.server_side.schedule;

import com.company.server_side.ServiceHandler;

public interface ServiceScheduler {
  void schedule(ServiceHandler handler);
}
