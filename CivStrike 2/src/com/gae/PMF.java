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
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");
    private PMF() {}
    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
