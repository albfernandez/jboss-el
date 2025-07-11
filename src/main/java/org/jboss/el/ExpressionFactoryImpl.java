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

package org.jboss.el;

import jakarta.el.ELContext;
import jakarta.el.ExpressionFactory;
import jakarta.el.MethodExpression;
import jakarta.el.ValueExpression;

import org.jboss.el.lang.ELSupport;
import org.jboss.el.lang.ExpressionBuilder;
import org.jboss.el.util.MessageFactory;


/**
 * @see javax.el.ExpressionFactory
 * 
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: markt $
 */
public class ExpressionFactoryImpl extends ExpressionFactory {

    /**
     * 
     */
    public ExpressionFactoryImpl() {
        super();
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T coerceToType(Object obj, Class<T> targetType) {
        return (T) ELSupport.coerceToType(obj, targetType);
    }

    @Override
	public MethodExpression createMethodExpression(ELContext context,
            String expression, Class<?> expectedReturnType,
            Class<?>[] expectedParamTypes) {
        if (expectedParamTypes == null) {
            throw new NullPointerException(MessageFactory
                    .get("error.method.nullParms"));
        }
        ExpressionBuilder builder = new ExpressionBuilder(expression, context);
        return builder.createMethodExpression(expectedReturnType,
                expectedParamTypes);
    }

    @Override
	public ValueExpression createValueExpression(ELContext context,
            String expression, Class<?> expectedType) {
        if (expectedType == null) {
            throw new NullPointerException(MessageFactory
                    .get("error.value.expectedType"));
        }
        ExpressionBuilder builder = new ExpressionBuilder(expression, context);
        return builder.createValueExpression(expectedType);
    }

    @Override
	public ValueExpression createValueExpression(Object instance,
            Class<?> expectedType) {
        if (expectedType == null) {
            throw new NullPointerException(MessageFactory
                    .get("error.value.expectedType"));
        }
        return new ValueExpressionLiteral(instance, expectedType);
    }
}
