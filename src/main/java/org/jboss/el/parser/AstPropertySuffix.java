/* Generated By:JJTree: Do not edit this line. AstPropertySuffix.java */

package org.jboss.el.parser;


import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.MethodInfo;
import javax.el.PropertyNotFoundException;

import org.jboss.el.lang.EvaluationContext;
import org.jboss.el.util.MessageFactory;
import org.jboss.el.util.ReflectionUtil;

public final class AstPropertySuffix extends ValueSuffixNode {

	public AstPropertySuffix(int id) {
		super(id);
	}

	public MethodInfo getMethodInfo(Object base, EvaluationContext ctx,
			Class[] paramTypes) throws ELException {
		return ReflectionUtil.getMethodInfo(base, this.image, paramTypes);
	}

	public Class getType(Object base, EvaluationContext ctx) throws ELException {
		ELResolver resolver = ctx.getELResolver();
		ctx.setPropertyResolved(false);
		return resolver.getType(ctx, base, this.image);
	}

	public Object getTarget(Object base, EvaluationContext ctx)
			throws ELException {
		if (base == null) {
			return null;
		}
		ELResolver resolver = ctx.getELResolver();
		ctx.setPropertyResolved(false);
		Object r = resolver.getValue(ctx, base, this.image);
		if (r == null) {
			throw new PropertyNotFoundException(MessageFactory.get(
					"error.unreachable.property", base.getClass().getName(),
					this.image));
		}
		return r;
	}

	public Object getValue(Object base, EvaluationContext ctx)
			throws ELException {
		if (base == null) {
			return null;
		}
		ELResolver resolver = ctx.getELResolver();
		ctx.setPropertyResolved(false);
		return resolver.getValue(ctx, base, this.image);
	}

	public Object invoke(Object base, EvaluationContext ctx,
			Class[] paramTypes, Object[] paramValues) throws ELException {
		return ReflectionUtil.invokeMethod(base, this.image, paramTypes,
				paramValues);
	}

	public boolean isReadOnly(Object base, EvaluationContext ctx)
			throws ELException {
		ELResolver resolver = ctx.getELResolver();
		ctx.setPropertyResolved(false);
		return resolver.isReadOnly(ctx, base, this.image);
	}

	public void setValue(Object base, EvaluationContext ctx, Object value)
			throws ELException {
		ELResolver resolver = ctx.getELResolver();
		ctx.setPropertyResolved(false);
		resolver.setValue(ctx, base, this.image, value);
	}
}
