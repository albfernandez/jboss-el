package org.jboss.el.lang;

import java.lang.reflect.Method;

import jakarta.el.FunctionMapper;

public abstract class ExtendedFunctionMapper extends FunctionMapper {

	public ExtendedFunctionMapper() {
		super();
	}

	public abstract Method resolveFunction(String prefix, String localName, int paramCount);
}
