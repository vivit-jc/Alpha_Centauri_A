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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemHeadersSupport;
import org.apache.commons.fileupload.ParameterParser;

@SuppressWarnings("serial")
public class MemoryFileItem implements FileItem, FileItemHeadersSupport {
    //private static final long serialVersionUID = -2531086814081783645L;
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    private String fieldName;
    private String contentType;
    private boolean isFormField;
    private String fileName;

    private long size = -1;
    private byte[] cachedContent = null;
    private FileItemHeaders headers = null;

    public MemoryFileItem (String fieldName, String contentType, boolean isFormField, String fileName) {
        this.fieldName = fieldName;
        this.contentType = contentType;
        this.isFormField = isFormField;
        this.fileName = fileName;
    }

    public void delete() {
        cachedContent = null;
        baos = null;
    }

    public byte[] get() {
        if (isInMemory()) {
            if (cachedContent == null) {
                cachedContent = baos.toByteArray();
            }
            return cachedContent;
        }
        return new byte[0];
    }

    public String getContentType() {
        return contentType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public InputStream getInputStream() throws IOException {
        if (cachedContent == null) {
            cachedContent = baos.toByteArray();
        }
        return new ByteArrayInputStream(cachedContent);
    }

    public String getName() {
        return fileName;
    }

    private ByteArrayOutputStream baos = null;

    public OutputStream getOutputStream() throws IOException {
        if (baos == null) {
            baos = new ByteArrayOutputStream();
        }
        return baos;
    }

    public long getSize() {
        if (size >= 0) {
            return size;
        } else if (cachedContent != null) {
            return cachedContent.length;
        } else if (baos != null) {
            return baos.toByteArray().length;
        }
        return 0;
    }

    public String getString() {
        byte[] rawdata = get();
        String charset = getCharSet();
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        try {
            return new String(rawdata, charset);
        } catch (UnsupportedEncodingException e) {
            return new String(rawdata);
        }
    }

    @SuppressWarnings("unchecked")
    public String getCharSet() {
        ParameterParser parser = new ParameterParser();
        parser.setLowerCaseNames(true);
        // Parameter parser can handle null input
        Map params = parser.parse(getContentType(), ';');
        return (String) params.get("charset");
    }

    public String getString(String encoding) throws UnsupportedEncodingException {
        return new String(get(), encoding);
    }

    public boolean isFormField() {
        return isFormField;
    }

    public boolean isInMemory() {
        return true;
    }

    public void setFieldName(String name) {
        this.fieldName = name;
    }

    public void setFormField(boolean state) {
        this.isFormField = state;
    }

    public void write(File file) throws Exception {
        // no process;
    }

    public FileItemHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(FileItemHeaders headers) {
        this.headers = headers;
    }

}
