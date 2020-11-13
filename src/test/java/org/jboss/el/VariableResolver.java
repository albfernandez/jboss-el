package org.jboss.el;

import java.beans.FeatureDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

public class VariableResolver extends ELResolver {
	
	private final Map<Object, Object> vars = new HashMap<Object, Object>();
	
	public void setValue(String name, Object value) {
		this.vars.put(name, value);
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
			Object base) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property)
			throws NullPointerException, PropertyNotFoundException, ELException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(ELContext context, Object base, Object property)
			throws NullPointerException, PropertyNotFoundException, ELException {
		if (base == null) {
			context.setPropertyResolved(true);
			return this.vars.get(property);
		}
		return null;
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property)
			throws NullPointerException, PropertyNotFoundException, ELException {
		return false;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property,
			Object value) throws NullPointerException,
			PropertyNotFoundException, PropertyNotWritableException,
			ELException {
		if (base == null) {
			context.setPropertyResolved(true);
			this.vars.put(property, value);
		}
	}
}
