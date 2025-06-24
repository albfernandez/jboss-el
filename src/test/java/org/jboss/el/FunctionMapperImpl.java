package org.jboss.el;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import jakarta.el.FunctionMapper;

public class FunctionMapperImpl extends FunctionMapper {
    
    protected Map<String, Method> functions;
    
    public FunctionMapperImpl() {}
    
    @Override
	public Method resolveFunction(String prefix, String localName) {
        if (this.functions == null) return null;
        
        String key = prefix + ":" + localName;
        return this.functions.get(key);
    }
    
    public void setFunction(String prefix, String localName, Method m) {
        String key = prefix + ":" + localName;
        if (this.functions == null) {
            this.functions = new HashMap<String,Method>();
        }
        
        this.functions.put(key, m);
    }
    
}
