/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.el.lang;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.el.ELContext;
import jakarta.el.ELException;
import jakarta.el.FunctionMapper;
import jakarta.el.MethodExpression;
import jakarta.el.ValueExpression;
import jakarta.el.VariableMapper;

import org.jboss.el.MethodExpressionImpl;
import org.jboss.el.MethodExpressionLiteral;
import org.jboss.el.ValueExpressionImpl;
import org.jboss.el.parser.AstCompositeExpression;
import org.jboss.el.parser.AstDeferredExpression;
import org.jboss.el.parser.AstDynamicExpression;
import org.jboss.el.parser.AstFunction;
import org.jboss.el.parser.AstIdentifier;
import org.jboss.el.parser.AstLiteralExpression;
import org.jboss.el.parser.AstValue;
import org.jboss.el.parser.ELParser;
import org.jboss.el.parser.Node;
import org.jboss.el.parser.NodeVisitor;
import org.jboss.el.parser.ParseException;
import org.jboss.el.util.ConcurrentCache;
import org.jboss.el.util.MessageFactory;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: markt $
 */
public final class ExpressionBuilder implements NodeVisitor {

    private static final int CACHE_SIZE;
    private static final String CACHE_SIZE_PROP =
        "org.jboss.el.lang.ExpressionBuilder.CACHE_SIZE";
 
    static {
        if (System.getSecurityManager() == null) {
            CACHE_SIZE = Integer.parseInt(
                    System.getProperty(CACHE_SIZE_PROP, "-1"));
        } else {
            CACHE_SIZE = AccessController.doPrivileged(
                    new PrivilegedAction<Integer>() {
                    	@Override
						public Integer run() {
                        return Integer.valueOf(
                                System.getProperty(CACHE_SIZE_PROP, "-1"));
                    }
                }).intValue();
        }
        if (CACHE_SIZE > 0) {
            unlimitedCache = null;
            cache = new ConcurrentCache<String, Node>(CACHE_SIZE);
        } else {
            cache = null;
            unlimitedCache = new ConcurrentHashMap<String, Node>(1024);
        }
    }

    private static final ConcurrentCache<String, Node> cache;
    private static final ConcurrentHashMap<String, Node> unlimitedCache;
	
	private FunctionMapper fnMapper;

	private VariableMapper varMapper;

	private String expression;

	/**
	 * @param expression the expression
     * @param ctx the context
	 */
	public ExpressionBuilder(String expression, ELContext ctx)
			throws ELException {
		this.expression = expression;

		FunctionMapper ctxFn = ctx.getFunctionMapper();
		VariableMapper ctxVar = ctx.getVariableMapper();

		if (ctxFn != null) {
			this.fnMapper = new FunctionMapperFactory(ctxFn);
		}
		if (ctxVar != null) {
			this.varMapper = new VariableMapperFactory(ctxVar);
		}
	}

	public final static Node createNode(String expr) throws ELException {
		return createNodeInternal(expr);
	}

	private final static Node createNodeInternal(String expr)
			throws ELException {
		if (expr == null) {
			throw new ELException(MessageFactory.get("error.null"));
		}

		Node n = (cache != null) ? cache.get(expr) : unlimitedCache.get(expr);
		if (n == null) {
			try {
				n = (new ELParser(new StringReader(expr)))
						.CompositeExpression();

				// validate composite expression
				if (n instanceof AstCompositeExpression) {
					int numChildren = n.jjtGetNumChildren();
					if (numChildren == 1) {
						n = n.jjtGetChild(0);
					} else {
						Class<?> type = null;
						Node child = null;
						for (int i = 0; i < numChildren; i++) {
							child = n.jjtGetChild(i);
							if (child instanceof AstLiteralExpression) {
								continue;
							}
							if (type == null) {
								type = child.getClass();
							}
							else {
								if (!type.equals(child.getClass())) {
									throw new ELException(MessageFactory.get(
											"error.mixed", expr));
								}
							}
						}
					}
				}
				if (n instanceof AstDeferredExpression
						|| n instanceof AstDynamicExpression) {
					n = n.jjtGetChild(0);
				}
				if (cache != null) {
				    cache.put(expr, n);
				} else {
				    unlimitedCache.put(expr, n);
				}
			} catch (ParseException pe) {
				throw new ELException("Error Parsing: " + expr, pe);
			}
		}
		return n;
	}

	private void prepare(Node node) throws ELException {
        try {
            node.accept(this);
        } catch (Exception e) {
            throw (ELException) e;
        }
		if (this.fnMapper instanceof FunctionMapperFactory) {
			this.fnMapper = ((FunctionMapperFactory) this.fnMapper).create();
		}
		if (this.varMapper instanceof VariableMapperFactory) {
			this.varMapper = ((VariableMapperFactory) this.varMapper).create();
		}
	}

	private Node build() throws ELException {
		Node n = createNodeInternal(this.expression);
		this.prepare(n);
		if (n instanceof AstDeferredExpression
				|| n instanceof AstDynamicExpression) {
			n = n.jjtGetChild(0);
		}
		return n;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.Node)
	 */
	@Override
	public void visit(Node node) throws ELException {
		if (node instanceof AstFunction) {

			

			if (this.fnMapper == null) {
				throw new ELException(MessageFactory.get("error.fnMapper.null"));
			}
			Method m = null;
			
			AstFunction funcNode = (AstFunction) node;
			if (fnMapper instanceof ExtendedFunctionMapper)
			{
			   m = ((ExtendedFunctionMapper) fnMapper).resolveFunction(funcNode.getPrefix(), 
			         funcNode.getLocalName(), node.jjtGetNumChildren());    
			}
			else
			{
			   m = fnMapper.resolveFunction(funcNode.getPrefix(), funcNode.getLocalName());
			}
			
			if (m == null) {
				throw new ELException(MessageFactory.get(
						"error.fnMapper.method", funcNode.getOutputName()));
			}
			int pcnt = m.getParameterTypes().length;
			if (!m.isVarArgs() && node.jjtGetNumChildren() != pcnt) {
				throw new ELException(MessageFactory.get(
						"error.fnMapper.paramcount", funcNode.getOutputName(),
						"" + pcnt, "" + node.jjtGetNumChildren()));
			}
		} else if (node instanceof AstIdentifier && this.varMapper != null) {
			String variable = ((AstIdentifier) node).getImage();

			// simply capture it
			this.varMapper.resolveVariable(variable);
		}
	}

	public ValueExpression createValueExpression(Class<?> expectedType)
			throws ELException {
		Node n = this.build();
		return new ValueExpressionImpl(this.expression, n, this.fnMapper,
				this.varMapper, expectedType);
	}

	public MethodExpression createMethodExpression(Class<?> expectedReturnType,
			Class<?>[] expectedParamTypes) throws ELException {
		Node n = this.build();
		if (n instanceof AstValue || n instanceof AstIdentifier) {
			return new MethodExpressionImpl(expression, n, this.fnMapper,
					this.varMapper, expectedReturnType, expectedParamTypes);
		} else if (n instanceof AstLiteralExpression) {
			return new MethodExpressionLiteral(expression, expectedReturnType,
					expectedParamTypes);
		} else {
			throw new ELException("Not a Valid Method Expression: "
					+ expression);
		}
	}
}
