package org.jboss.el;

import jakarta.el.ArrayELResolver;
import jakarta.el.BeanELResolver;
import jakarta.el.CompositeELResolver;
import jakarta.el.ELContext;
import jakarta.el.ELResolver;
import jakarta.el.FunctionMapper;
import jakarta.el.ListELResolver;
import jakarta.el.MapELResolver;
import jakarta.el.VariableMapper;

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
