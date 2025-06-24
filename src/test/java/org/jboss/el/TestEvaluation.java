package org.jboss.el;

import java.util.HashMap;
import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import org.jboss.el.beans.Example;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestEvaluation {
    
    private ExpressionFactory factory = null;
    private ELContextImpl context = null;
    
    @Test
    public void testListeners() throws Exception {
        for (int i = 0; i < 5; i++) {
            evalMethod("#{company.departments.{x|x.employees.{x|x.sayHello}}}", new Class[] { String.class},  "Holden");
        }
    }
    
    @Test
    public void testPerformance() throws Exception {
        ValueExpression ve = this.factory.createValueExpression(this.context, "#{company.departments.{x|x.employees.{x|x.lastName}}}", Object.class);
        
        Map<String, Object> ctx = new HashMap<String, Object>();
        ctx.put("company", Example.createCompany());
        
        int runs = 10000;
        Object value = ve.getValue(this.context);
        long startNano = System.nanoTime();
        for (int i = 0; i < runs; i++) {
            value = ve.getValue(this.context);
        }
        long endNano = System.nanoTime();
        long totalMillis = (endNano - startNano) / 1000000L;
        System.out.println("New [" + (totalMillis/(double) runs) + "] " + value);
    }
    @Test
    public void testSetters() throws Exception {
        for (int i = 0; i < 5; i++) {
            evalSetter("#{company.departments.{x|x.employees.{x|x.lastName}}}", "Holden");
        }
    }
    @Test
    public void testEvaluation() throws Exception {
        for (int i = 0; i < 5; i++) {
            eval("#{company.departments}");
            eval("#{company.getDepartments()}");
            eval("#{company.departments.{x|x.employees}}");
            eval("#{company.departments.{x|x.employees.{x|x}}}");
            eval("#{company.departments.{x|x.director}.{x|x.firstName}}");
            eval("#{company.departments.{x|x.employees.{x|x.lastName}}}");
            eval("#{company.departments.{x|x.employees.{x|x.sayHello(name)}}}");
        }
    }
    @Test
    public void testMethodExpressions() throws Exception {
        for (int i = 0; i < 5; i++) {
            evalMethod("#{company.departments[0].employees[0].sayHello(name)}");
            evalMethod("#{company.departments[0].employees[1].sayHello(name)}");
            evalMethod("#{company.departments[1].employees[0].sayHello(name)}");
            evalMethod("#{company.departments[1].employees[1].sayHello(5)}");
        }
    }
        
    public void evalSetter(String expr, Object value) {
        int runs = 10000;
        
        ValueExpression ve = this.factory.createValueExpression(this.context, expr, String.class);
        ve.getValue(this.context);
        long startNano = System.nanoTime();
        for (int i = 0; i < runs; i++) {
            ve.setValue(this.context, value);
        }
        long endNano = System.nanoTime();
        long usedMillis = (endNano - startNano) / 1000000L;
        System.out.println("New [" + ( usedMillis/(double) runs) + "] " +  expr + " " + ve.getValue(this.context));
    }
    
    public void evalMethod(String expr) throws Exception {
        this.evalMethod(expr, new Class[0]);
    }

    public void evalMethod(String expr, Class<?>[] types, Object... args) throws Exception {
        int runs = 10000;
        
        MethodExpression me = this.factory.createMethodExpression(this.context, expr, String.class, types);
        Object out = me.invoke(this.context, args);
        long startNano = System.nanoTime();
        for (int i = 0; i < runs; i++) {
            out = me.invoke(this.context, args);
        }
        long endNano = System.nanoTime();
        long usedMillis = (endNano - startNano) / 1000000L;
        System.out.println("New [" + (usedMillis/(double) runs) + "] " +  expr + " " + out);
    }
    
    
    public void eval(String expr) throws Exception {
        int runs = 10000;
        
        ValueExpression ve = this.factory.createValueExpression(this.context, expr, Object.class);
        Object value = ve.getValue(this.context);
        long startNano = System.nanoTime();
        for (int i = 0; i < runs; i++) {
            value = ve.getValue(this.context);
        }
        long endNano = System.nanoTime();
        long usedMillis = (endNano - startNano) / 1000000L;
        System.out.println("New [" + (usedMillis /(double) runs) + "] " +  expr + " " + value);
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        this.factory = new ExpressionFactoryImpl();
        this.context = new ELContextImpl();
        this.context.setVar("company", Example.createCompany());
        this.context.setVar("name", "Jacob");
        System.out.println("\n===============================================\n");
    }
    
}
