package com.company.server_side.FileService;

import com.company.server_side.Service.ServiceStatus;

public interface FileServiceListener extends ChangeableStatus {

  @Override
  void stop();

  /**
   * Returns the Status at the Service.
   *
   * <p>Return <@code>STATUS_UP</@code> if the service running otherwise return <@code>STATUS_DOWN
   * </@code>
   *
   * @throws NullPointerException {@inheritDoc}
   */
  ServiceStatus getStatus();

  /**
   * Set status of this Object to status
   *
   * @param status of type <@code>ServiceStatus</@code> to be set.
   */
  void setStatus(ServiceStatus status);
}
