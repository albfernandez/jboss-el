package org.jboss.el.beans;

public final class Employee {

    private String firstName;
    private String lastName;
    private long id;
    private boolean management;
    
    public Employee() {
        super();
    }
    
    public Employee(long id, String lastName, String firstName, boolean management) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.management = management;
    }
    
    public String sayGoodbye(String in) {
        return "Goodbye " + in + ", my name is " + this.getFirstName() + " " + this.getLastName();
    }
    
    public String sayHello(String in) {
        return "Hello " + in + ", my name is " + this.getFirstName() + " " + this.getLastName();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isManagement() {
        return management;
    }

    public void setManagement(boolean management) {
        this.management = management;
    }
    
    @Override
	public String toString() {
        return "Employee[" + this.lastName + "," + this.firstName + "]";
    }

}
