package com.company.server_side.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ServerConfig {

  public static final int SERVER_PORT_NUMBER = 2222;
  public static final int MAX_NUMBER_CLIENTS = 100;
  public static final String SERVER_FILES_TO_TEST = "ServerFilesToTest";
  public static final Path CLIENT_FOLDER_PATH =
      Paths.get((System.getProperty("user.dir") + File.separator + SERVER_FILES_TO_TEST));

  private ServerConfig() {
    throw new UnsupportedOperationException("Can’t Create Object From: " + getClass().getName());
  }
}
