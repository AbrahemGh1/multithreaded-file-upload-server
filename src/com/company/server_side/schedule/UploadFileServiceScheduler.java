package com.company.server_side.schedule;
//Marker interfaces

/**
 * Classes implement <@code>UploadFileServiceScheduler</@code> mean is capable of being scheduling
 * with other FileServiceScheduler category.
 *
 * <p>In other word every subclasses implement <@code>Handler<code/> and  attend to handle
 * any <@code>FileService</code> need to  implement <@code>UploadFileServiceScheduler</code>.
 * </p>
 */

interface UploadFileServiceScheduler extends FileServiceScheduler {

}
