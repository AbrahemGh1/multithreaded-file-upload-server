package com.company.serverSide;

public interface Changeable extends Runnable {
  /**
   * Returns the state of this Service.
   * This method is designed for use in monitoring of the system state,
   * not for synchronization control.
   *
   * @return this Service's state.
   */
  public void stop();
}
