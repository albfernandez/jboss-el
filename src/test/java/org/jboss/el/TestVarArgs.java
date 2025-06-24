/*
 * TestVarArgs.java
 *
 * Created on December 10, 2006, 12:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jboss.el;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.el.ValueExpression;

import org.jboss.el.beans.Security;
import org.jboss.el.parser.ELParser;
import org.jboss.el.parser.SimpleNode;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jhook
 */
public class TestVarArgs {
    
	
	@Test
    public void main() throws Exception {
        Class<?> c = Security.class;
        Method[] m = c.getMethods();
        
        FunctionMapperImpl fn = new FunctionMapperImpl();
        
        for (int i = 0; i < m.length; i++) {
            if (Modifier.isStatic(m[i].getModifiers())) {
            	System.out.println("Found Method: " + m[i]);
                fn.setFunction("", m[i].getName(), m[i]);
            }
        }
        
        ELContextImpl impl = new ELContextImpl(fn);
        ExpressionFactoryImpl ef = new ExpressionFactoryImpl();
        impl.setVar("user", new Security());
        
        String[] tests = new String[] { "#{hasRole(user, 1, 2, 3)}", "#{hasRole(user, 1)}", "#{hasOneRole(1)}", "#{user.matchRole(1)}", "#{user.matchRole(1,3,4,5)}" };
        for (String el : tests) {
        	parse(el);
        	ValueExpression ve = ef.createValueExpression(impl, el, Object.class);
        	Object obj = ve.getValue(impl);
        	System.out.println("\n=================\n" + obj + "\n=========================\n");
        }
        
    }
    
    public static SimpleNode parse(String in) {
		System.out.println(in);
		SimpleNode node = (SimpleNode) ELParser.parse(in);
		node.dump("");
		System.out.println();
		return node;
	}
}
