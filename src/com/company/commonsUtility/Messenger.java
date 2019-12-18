package com.company.commonsUtility;

import org.jetbrains.annotations.NotNull;

public interface Messenger {

  /**
   * Write a <@code>String</@code> message on messenger.
   *
   * @param message to be written on messenger.
   */
  void writeMessage(@NotNull String message);

  /**
   * Read next message from messenger and return it as a <@code>String</@code>.
   *
   * @return String represent the message reed from messenger.
   */
  String readMessage();

  /**
   * close connection
   */
  void close();
}
