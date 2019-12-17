package com.company.server_side.Service;


public final class ServerConfig {

  public static final int SERVER_PORT_NUMBER = 2022;
  public static final int MAX_NUMBER_CLIENTS = 10;


  private ServerConfig() {
    throw new UnsupportedOperationException("Can’t Create Object From: " + getClass().getName());
  }

}





