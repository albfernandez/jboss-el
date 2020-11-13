package org.jboss.el;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.VariableMapper;

public class ELContextImpl extends ELContext {

	private CompositeELResolver resolver = new CompositeELResolver();
	private VariableResolver vars = new VariableResolver();
	private VariableMapper vm = new VariableMapperImpl();
	private FunctionMapper fm = new FunctionMapperImpl();
	
	public ELContextImpl() {
		resolver.add(this.vars);
		resolver.add(new MapELResolver());
		resolver.add(new ArrayELResolver());
		resolver.add(new ListELResolver());
		resolver.add(new BeanELResolver());
	}
	
	public ELContextImpl(FunctionMapper fm) {
		resolver.add(this.vars);
		resolver.add(new MapELResolver());
		resolver.add(new ArrayELResolver());
		resolver.add(new ListELResolver());
		resolver.add(new BeanELResolver());
		this.fm = fm;
	}
	
	@Override
	public ELResolver getELResolver() {
		return this.resolver;
	}
	
	public void setVar(String name, Object value) {
		this.vars.setValue(name, value);
	}

	@Override
	public FunctionMapper getFunctionMapper() {
		return this.fm;
	}

	@Override
	public VariableMapper getVariableMapper() {
		return vm;
	}

}
