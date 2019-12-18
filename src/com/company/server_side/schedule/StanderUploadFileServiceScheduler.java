package com.company.server_side.schedule;

import com.company.server_side.handler.ServiceHandler;
import java.util.Objects;

/**
 * StanderUploadFileServiceScheduler implementation of the <tt>UploadFileServiceScheduler</tt>
 * interface.
 *
 * @author Ibrahim Gharayiba
 * @see UploadFileServiceScheduler
 */
public class StanderUploadFileServiceScheduler extends UploadFileServiceScheduler {

  /**
   * Add handler object that implement <@code>ServiceHandler</code> to schedule by invoking submit
   * method.
   *
   * <p>In other word any handler wish to start executed in separated thread need to implement
   * <@code>ServiceHandler</code> and register the object to scheduler by invoking schedule method.
   *
   * @param handler the Object to be added to schedule.
   * @throws NullPointerException if the specified handler is null.
   */
  @Override
  public void schedule(ServiceHandler handler) {
    Objects.requireNonNull(handler);
    executor.submit(handler::serviceHandler);
  }
}
