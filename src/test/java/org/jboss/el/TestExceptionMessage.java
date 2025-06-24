package org.jboss.el;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.PropertyNotFoundException;

import org.jboss.el.beans.Company;
import org.jboss.el.lang.ExpressionBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

public class TestExceptionMessage {

    private ExpressionFactory factory = null;
    private ELContextImpl context = null;
    
    public TestExceptionMessage() {
    	super();
    }
    
    @Test
    public void test1() {
    	Assertions.assertThrows(PropertyNotFoundException.class, () -> {
	    	String expression = "#{company.president.firstName}";
	    	 MethodExpression me = this.factory.createMethodExpression(this.context, expression, String.class, new Class[0]);
	         Object out = me.invoke(this.context, new Object[]{});
	         System.out.println("out:" + out);
    	});
    }
    
    @Test
    public void test2() {
    	String expression = "#{company.president.firstName}";
    	ExpressionBuilder eb = new ExpressionBuilder(expression, this.context);
    	Object out = eb.createValueExpression(String.class).getValue(this.context);
//    	 MethodExpression me = this.factory.createMethodExpression(this.context, expression, String.class, new Class[0]);
//         Object out = me.invoke(this.context, new Object[]{});
         System.out.println("out:" + out);
    }
    
	@BeforeEach
	public void setUp() {
		this.factory = new ExpressionFactoryImpl();
		// this.sun = new com.sun.el.ExpressionFactoryImpl();
		this.context = new ELContextImpl();
		this.context.setVar("company", new Company());
		this.context.setVar("name", "Jacob");
		System.out.println("\n===============================================\n");
	}
}
