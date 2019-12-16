package com.company.server_side.FileService;

/**
 * The <code>ChangeableStatus</code> interface should be implemented by any
 * <code>Service</code> class whose intended to be executed by a thread. The
 * class must implements two method of no arguments called <code>stop</code> and <code>run</>.
 * <p>
 * This interface is designed to provide a common protocol for any objects implements
 * <code>FileServiceListener</code> interface that wish to be handle as a Service.
 * <p/>
 * In other word this<>ChangeableStatus</> provide the capability to service to change from running state
 * to stop state.<p/>
 *
 * <p><code>Run<code/> method is overrides from <>Runnable</> interface <p/>
 *
 * @author Ibrahem Gharayiba
 * @see java.lang.Thread
 * @see java.util.concurrent.Callable
 * @see java.lang.Runnable
 */

public interface ChangeableStatus extends Runnable {

  /**
   * Stop the service from running by attempt to calling setStatus(SERVICE_DOWN) method for
   * <code>this</code> object
   */
  void stop();
}
