/* Generated By:JJTree: Do not edit this line. AstLiteralExpression.java */

package org.jboss.el.parser;

import javax.el.ELException;

import org.jboss.el.lang.EvaluationContext;


/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: markt $
 */
public final class AstLiteralExpression extends SimpleNode {
    public AstLiteralExpression(int id) {
        super(id);
    }

    @Override
	public Class getType(EvaluationContext ctx) throws ELException {
        return String.class;
    }

    @Override
	public Object getValue(EvaluationContext ctx) throws ELException {
        return this.image;
    }

    @Override
	public void setImage(String image) {
        if (image.indexOf('\\') == -1) {
            this.image = image;
            return;
        }
        int size = image.length();
        StringBuffer buf = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            char c = image.charAt(i);
            if (c == '\\' && i + 1 < size) {
                char c1 = image.charAt(i + 1);
                if (c1 == '\\' || c1 == '"' || c1 == '\'' || c1 == '#'
                        || c1 == '$') {
                    c = c1;
                    i++;
                }
            }
            buf.append(c);
        }
        this.image = buf.toString();
    }
}
