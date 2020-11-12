/* Generated By:JJTree: Do not edit this line. AstClosure.java */

package org.jboss.el.parser;

import javax.el.ELException;
import javax.el.MethodInfo;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import org.jboss.el.ValueExpressionLiteral;
import org.jboss.el.lang.EvaluationContext;

public final class AstClosure extends ValueSuffixNode {
    public AstClosure(int id) {
        super(id);
    }
    
    public boolean isReadOnly(Object base, EvaluationContext ctx) throws ELException {
        VariableMapper orig = ctx.getVariableMapper();
        ctx.setVariableMapper(new ClosureVariableMapper(orig, this.image, base));
        boolean result = false;
        try {
            result = this.children[0].isReadOnly(ctx);
        } finally {
            ctx.setVariableMapper(orig);
        }
        return result;
    }
    
    public Object getValue(Object base, EvaluationContext ctx) throws ELException {
        VariableMapper orig = ctx.getVariableMapper();
        ctx.setVariableMapper(new ClosureVariableMapper(orig, this.image, base));
        Object result = null;
        try {
            result = this.children[0].getValue(ctx);
        } finally {
            ctx.setVariableMapper(orig);
        }
        return result;
    }
    
    public Class getType(Object base, EvaluationContext ctx) throws ELException {
        VariableMapper orig = ctx.getVariableMapper();
        ctx.setVariableMapper(new ClosureVariableMapper(orig, this.image, base));
        Class result = null;
        try {
            result = this.children[0].getType(ctx);
        } finally {
            ctx.setVariableMapper(orig);
        }
        return result;
    }
    
    public Object getTarget(Object base, EvaluationContext ctx) throws ELException {
        return null;
    }
    
    public MethodInfo getMethodInfo(Object base, EvaluationContext ctx, Class[] paramTypes) throws ELException {
        VariableMapper orig = ctx.getVariableMapper();
        ctx.setVariableMapper(new ClosureVariableMapper(orig, this.image, base));
        MethodInfo result = null;
        try {
            result = this.children[0].getMethodInfo(ctx, paramTypes);
        } finally {
            ctx.setVariableMapper(orig);
        }
        return result;
    }
    
    public Object invoke(Object base, EvaluationContext ctx, Class[] paramTypes, Object[] paramValues) throws ELException {
        VariableMapper orig = ctx.getVariableMapper();
        ctx.setVariableMapper(new ClosureVariableMapper(orig, this.image, base));
        Object result = null;
        try {
            result = this.children[0].invoke(ctx, paramTypes, paramValues);
        } finally {
            ctx.setVariableMapper(orig);
        }
        return result;
    }
    
    public void setValue(Object base, EvaluationContext ctx, Object value) throws ELException {
        VariableMapper orig = ctx.getVariableMapper();
        ctx.setVariableMapper(new ClosureVariableMapper(orig, this.image, base));
        try {
            this.children[0].setValue(ctx, value);
        } finally {
            ctx.setVariableMapper(orig);
        }
    }
    
    private class ClosureVariableMapper extends VariableMapper {
        private final String var;
        private final ValueExpression value;
        private final VariableMapper orig;
        
        public ClosureVariableMapper(VariableMapper map, String var, Object value) {
        	super();
            this.orig = map;
            this.var = var;
            this.value = new ValueExpressionLiteral(value, Object.class);
        }

        public ValueExpression resolveVariable(String variable) {
            if (this.var.equals(variable)) {
                return this.value;
            } else if (this.orig != null) {
                return this.orig.resolveVariable(variable);
            } else {
                return null;
            }
        }

        public ValueExpression setVariable(String variable, ValueExpression expression) {
            throw new UnsupportedOperationException();
        }
    }
    
}
