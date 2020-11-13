package org.jboss.el.beans;

import java.util.ArrayList;
import java.util.List;

public final class Department {

    private String name;
    private List<Employee> employees;
    private Employee director;
    
    public Department() {
        super();
        this.employees = new ArrayList<Employee>();
    }

    public Employee getDirector() {
        return director;
    }

    public void setDirector(Employee director) {
        this.director = director;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
	public String toString() {
        return "Department[" + this.getName() + "]";
    }

}
