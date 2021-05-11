package il.ac.haifa.cs.sweng.cms.entities;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class Employee extends User {
	private String passkey;
	private int permission;
	
	public Employee() {
		super();
	}
	public Employee(String firstName, String lastName, String passkey, int permission) {
		super(firstName,lastName);
		this.passkey=passkey;
		this.permission=permission;
	}
	
	public String getPasskey() {return passkey;}
	
	public void setPasskey(String passkey) {this.passkey = passkey;}
	
	public int getPermission() {return permission;}
	
	public void setPermission(int permission) {this.permission = permission;}
	
}
