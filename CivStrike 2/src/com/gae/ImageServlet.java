/*!
* gaedirect v3.2.0
* *
* Copyright 2012, Katsuyuki Seino
* Licensed under the GPL Version 2 licenses.
* http://jquery.org/license
*
* Date: Mon May 29 2012
*/
package com.gae;
import java.io.IOException;
import java.io.InputStream;
//import java.net.URLDecoder;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

//import com.google.appengine.api.blobstore.BlobKey;
//import com.google.appengine.api.blobstore.BlobstoreServicePb.BlobstoreService;
//import com.google.appengine.api.images.Image;
//import com.google.appengine.api.images.ImagesServicePb.Transform;
//import com.google.appengine.api.images.ImagesService;
//import com.google.appengine.api.images.ImagesServiceFactory;
//import com.google.appengine.api.images.Transform;
//import com.google.appengine.api.images.ImagesService.OutputEncoding;
import javax.servlet.ServletOutputStream;

@SuppressWarnings("serial")
public class ImageServlet extends HttpServlet {
	@SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MemoryFileItemFactory factory = new MemoryFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        //resp.setContentType("image/png");
        ServletOutputStream out = resp.getOutputStream();
        try {
            List<FileItem> list = upload.parseRequest(req);
            for (FileItem item : list) {
                if (!(item.isFormField())) {
                    String fileName = item.getName();
                                        
                    //Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
                    //BlobKey blobKey = blobs.get("myFile");                 
                    
                    if (fileName != null && !"".equals(fileName)) {           	
                        int size = (int) item.getSize();
                        byte[] data = new byte[size];
                        InputStream in = item.getInputStream();
                        in.read(data);
                        //ImagesService imagesService = ImagesServiceFactory.getImagesService(); 
                        
                        /*
                        Image oldImage = ImagesServiceFactory.makeImage(data); 
                        Transform resize = ImagesServiceFactory.makeResize(180, 150); 
                        Image newImage = imagesService.applyTransform(resize, oldImage, OutputEncoding.PNG); 
                        byte[] newImageData = newImage.getImageData();
                        out.write(newImageData);
                        */   
                        /*
                        Image image = ImagesServiceFactory.makeImage(data); 
                        byte[] ImageData = image.getImageData();
                        */                                 
                        out.flush();
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
