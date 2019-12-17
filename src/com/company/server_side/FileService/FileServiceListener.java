package com.company.server_side.FileService;

import com.company.server_side.Service.ServiceStatus;

public interface FileServiceListener extends ChangeableStatus {

  @Override
  void run();

  @Override
  void stop();

  ServiceStatus getStatus();

  void setStatus(ServiceStatus status);
}
