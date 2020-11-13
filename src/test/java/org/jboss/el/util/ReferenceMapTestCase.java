/*
 * ReferenceMapTestCase.java
 *
 * Created on December 16, 2006, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jboss.el.util;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jhook
 */
public class ReferenceMapTestCase {
    
    public static class Foo {
        private final int id;
        private static int uid = 0;
        public Foo() { this.id = uid++; }
        public Foo(int id) { this.id = id; }
        @Override
		public String toString() {
            return "Foo[" + this.id + "]";
        }
        @Override
		public boolean equals(Object obj) {
            return obj instanceof Foo && ((Foo) obj).id == this.id;
        }
        @Override
		public int hashCode() { return this.id; }

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
		}
        
    }
    
    public static class Bar {
        private final int id;
        private static int uid = 0;
        public Bar() { this.id = uid++; }
        public Bar(int id) { this.id = id; }
        @Override
		public String toString() {
            return "Bar[" + this.id + "]";
        }

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
		}
    }
    
    public ReferenceMapTestCase() {
    	super();
    }
    
    @Test
    public void testReferences() throws Exception {
        
        ReferenceCache<Foo,Bar> map = new ReferenceCache<Foo,Bar>(ReferenceCache.Type.Weak, ReferenceCache.Type.Weak) {
            @Override
			protected Bar create(Foo key) {
            	return null;
            }
        };
        
        
        Collection<Foo> keys = new ArrayList<Foo>();
        for (int i = 0; i < 1000; i++) {
            keys.add(new Foo(i));
        }
        
        for (Foo f : keys) {
            map.put(f, new Bar(f.id));
        }
        
        
        for (Foo f : keys) {
            Assert.assertNotNull("Key not null " + f, map.get(f));
        }
        
        // comment and uncomment this line
        keys.clear();
        keys = null;
        
        
        // Force to GC to reclaim objects
        System.gc();
        
        
        keys = new ArrayList<Foo>();
        for (int i = 0; i < 1000; i++) {
            keys.add(new Foo(i));
        }
        
        for (Foo f : keys) {
            Assert.assertNull("Key null " + f, map.get(f));
        }
        
        
    }
    
}
