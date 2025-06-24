package org.jboss.el;


import org.jboss.el.parser.ELParser;
import org.jboss.el.parser.SimpleNode;
import org.junit.jupiter.api.Test;

public class TestParsing  {
	
	@Test
	public void testParsing() throws Exception {
		parse("#{foo.a.b}");
		parse("#{foo.a['b']}");
		parse("#{3 * foo.a.b(e,c,d)}");
		parse("#{'foo'.length().food}");
		parse("#{foo}");
//		parse("#{company.employees@name}");
//		parse("#{company.employees@getName()}");
//		parse("#{company.employees@each{x|x.salary}.salary}");
//		parse("#{company.employees@each{x|x.hashCode()}}");
//		parse("#{company.employees@each{x|x@max{y|y.salary}}} is complex");
                parse("#{company.get(foo)}");
		parse("#{company.employees.{x|x.name}}");
		parse("#{company.employees.{x|x.getName()}}");
		parse("#{company.employees.{x|x.salary}.salary}");
		parse("#{company.employees.{x|x.hashCode()}}");
		parse("#{company.employees.{x|x.{y|y.salary}}} is complex");
                
                System.out.println("\n=============================\n");
                parse("#{company.departments.{x|x.employees}.{x|x.lastName}}");
                parse("#{company.departments.{x|x.employees.{x|x.lastName}}}");
                parse("#{user.matchRole(1)}");
                parse("#{user.matchRole(1,4,3)}");
	}

	public static SimpleNode parse(String in) {
		System.out.println(in);
		SimpleNode node = (SimpleNode) ELParser.parse(in);
		node.dump("");
		System.out.println();
		return node;
	}
}
