package com.company.server_side.schedule;

import com.company.server_side.handler.ServiceHandler;

/**
 * <@code>FileServiceScheduler</@code> the root of all Scheduler object.
 */

public interface FileServiceScheduler {

  /**
   * Add any Object implement <@code>ServiceHandler</code>
   *
   * @param handler the Object to be add to
   */

  void schedule(ServiceHandler handler);
}
