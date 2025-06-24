package org.jboss.el;

import jakarta.el.ELContext;

public abstract class MethodResolver {
	protected MethodResolver() {
		super();
	}

	public abstract Object invoke(ELContext context, Object base, String name, Object[] params) throws Exception;
}
