/* Generated By:JJTree: Do not edit this line. AstNull.java */

package org.jboss.el.parser;

import javax.el.ELException;

import org.jboss.el.lang.EvaluationContext;


/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: markt $
 */
public final class AstNull extends SimpleNode {
    public AstNull(int id) {
        super(id);
    }

    @Override
	public Class getType(EvaluationContext ctx)
            throws ELException {
        return null;
    }

    @Override
	public Object getValue(EvaluationContext ctx)
            throws ELException {
        return null;
    }
}
