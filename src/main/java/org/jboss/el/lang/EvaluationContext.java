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

import jakarta.el.ELContext;
import jakarta.el.ELResolver;
import jakarta.el.FunctionMapper;
import jakarta.el.VariableMapper;

public final class EvaluationContext extends ELContext {

    private final ELContext elContext;

    private final FunctionMapper fnMapper;

    private VariableMapper varMapper;

    public EvaluationContext(ELContext elContext, FunctionMapper fnMapper,
            VariableMapper varMapper) {
    	super();
        this.elContext = elContext;
        this.fnMapper = fnMapper;
        this.varMapper = varMapper;
    }

    public ELContext getELContext() {
        return this.elContext;
    }

    @Override
	public FunctionMapper getFunctionMapper() {
        return this.fnMapper;
    }

    @Override
	public VariableMapper getVariableMapper() {
        return this.varMapper;
    }

    @Override
	public Object getContext(Class key) {
        return this.elContext.getContext(key);
    }

    @Override
	public ELResolver getELResolver() {
        return this.elContext.getELResolver();
    }

    @Override
	public boolean isPropertyResolved() {
        return this.elContext.isPropertyResolved();
    }

    @Override
	public void putContext(Class key, Object contextObject) {
        this.elContext.putContext(key, contextObject);
    }

    @Override
	public void setPropertyResolved(boolean resolved) {
        this.elContext.setPropertyResolved(resolved);
    }
    
    public void setVariableMapper(VariableMapper varMapper) {
        this.varMapper = varMapper;
    }
        
}
