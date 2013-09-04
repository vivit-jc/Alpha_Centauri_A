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
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Blob;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Imagedat {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key imgkey;    
	@Persistent
    private String guestname;
	@Persistent
    private String email;
	@Persistent
    private String filetitle;
	@Persistent
    private String comment;	
	@Persistent
    private String fileName;	
	@Persistent
    private Blob content;
    @Persistent
    private Date date;

    public Imagedat(Key imgkey,
    		        String guestname, 
    				String email, 
    				String filetitle,
    				String comment,
    				String fileName,
    				Blob content, 
    				Date date) {
    	this.imgkey = imgkey; 
    	this.guestname = guestname;
        this.email = email;
        this.filetitle = filetitle;
        this.comment = comment;
        this.fileName = fileName;
        this.content = content;
        this.date = date;
    }
    
    public Key getImgkey() { return imgkey; }
    public String getGuestname() { return guestname; }
    public String getEmail() { return email; }
    public String getFiletitle() { return filetitle; }
    public String getComment() { return comment; }
    public String getFilename() { return fileName; }
    public Blob getContent() { return content; }
    public Date getDate() { return date; }
    
    public void setImgkey(Key imgkey) { this.imgkey = imgkey; }
    public void setGuestname(String guestname) { this.guestname = guestname; }
    public void setEmail(String email) { this.email = email; }
    public void setFiletitle(String filetitle) { this.filetitle = filetitle; }
    public void setComment(String comment) { this.comment = comment; }
    public void setFilename(String fileName) { this.fileName = fileName; }
    public void setContent(Blob content) { this.content = content; }
    public void setDate(Date date) { this.date = date; }
}

