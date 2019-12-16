package com.company.server_side.any;

/**
 * <p>interface {@code Service} provide a basic operation can be done
 * on any services<p/>
 *
 * @author Ibrahim Gharayiba
 */
public interface Service {


  /**
   * Attempt to shutdown the service by calling  Stop() method for
   * <code>this</code> object
   * Subclasses of <code>Thread</code> should override Stop() method.
   */
  void shutdownService();


  /**
   * Returns {@code SERVICE_UP} if the provided reference has {@code SERVICE_UP} otherwise returns
   * {@code SERVICE_DOWN}.
   * <p>In other word return SERVICE_UP if the service running and return SERVICE_DOWN
   * shutdown or not enter a running state</p>
   *
   * @return {@code SERVICE_UP} if the provided reference has {@code SERVICE_UP} otherwise {@code
   * SERVICE_DOWN}
   */
  ServiceStatus getServiceStatus();

  /**
   * attempt to start the service by calling  thread.start() method for
   * <code>this</code> object
   * Subclasses of <code>Thread</code> should override Run method.
   */
  void startService();


}
