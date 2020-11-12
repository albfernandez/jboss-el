/*
 * Security.java
 *
 * Created on December 9, 2006, 2:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jboss.el.beans;

/**
 *
 * @author jhook
 */
public class Security {
    
    /** Creates a new instance of Security */
    public Security() {
    	super();
    }
    
    public static final boolean hasRole(Object in, String... match) {
        return true;
    }
    
    public static final boolean hasOneRole(String s) {
    	return true;
    }
    
    public boolean matchRole(String... match) {
        return true;
    }
}
