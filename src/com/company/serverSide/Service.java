package com.company.serverSide;


import org.jetbrains.annotations.NotNull;

public interface Service {

  void runService();
  void shutdownService();

  /**
   * Returns the state of this Service.
   * This method is designed for use in monitoring of the system state,
   * not for synchronization control.
   *
   * @return this Service's state.
   */
  @NotNull Status getServiceStatus();

}
