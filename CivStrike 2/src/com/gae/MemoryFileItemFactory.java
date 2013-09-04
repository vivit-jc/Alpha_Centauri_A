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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
public class MemoryFileItemFactory implements FileItemFactory {
    public MemoryFileItemFactory() {
    }
    public FileItem createItem(String fieldName, String contentType, boolean isFormField,
            String fileName) {
        MemoryFileItem result = new MemoryFileItem(fieldName, contentType, isFormField, fileName);
        return result;
    }
}