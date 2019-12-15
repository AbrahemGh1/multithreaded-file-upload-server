package com.company.serverSide;


public interface Service {

  void shutdownService();

  Status getServiceStatus();

  void runService();


}
