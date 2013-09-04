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
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class blobinfo {
  @PrimaryKey
  private String key;
  @Persistent
  private String blobskey;
  
  public blobinfo(String key, String blobskey) {
    this.key = key;
    this.blobskey = blobskey;
  }

  public String getBlobname() { return key; }
  public String getBlobskey() { return blobskey; }
    
  public void setBlobname(String key) { this.key = key; }
  public void setBlobskey(String blobskey) { this.blobskey = blobskey; }
}

