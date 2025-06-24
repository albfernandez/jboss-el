package org.jboss.el;

import java.util.HashMap;
import java.util.Map;

import jakarta.el.ValueExpression;
import jakarta.el.VariableMapper;

public class VariableMapperImpl extends VariableMapper {
	
	private final Map<String, ValueExpression> vars = new HashMap<String, ValueExpression>();

	@Override
	public ValueExpression resolveVariable(String variable) {
		return this.vars.get(variable);
	}

	@Override
	public ValueExpression setVariable(String variable,
			ValueExpression expression) {
		return this.vars.put(variable, expression);
	}

}
