package com.company.server_side.schedule;
// Marker interfaces

import static com.company.server_side.Service.ServerConfig.MAX_NUMBER_CLIENTS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classes implement <@code>UploadFileServiceScheduler</@code> mean is capable of being scheduling
 * with other FileServiceScheduler category.
 *
 * <p>In other word every subclasses implement <@code>Handler<code/> and  attend to handle
 * any <@code>FileService</code> need to implement <@code>UploadFileServiceScheduler</code>.
 *
 * @author Ibrahim Gharayiba
 */
abstract class UploadFileServiceScheduler implements FileServiceScheduler {

  static final ExecutorService executor = Executors.newFixedThreadPool(MAX_NUMBER_CLIENTS);
}
