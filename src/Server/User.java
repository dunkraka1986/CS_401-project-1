package Server;

import java.io.Serializable;

enum Role {STUDENT, ADMIN}

public abstract class User implements Serializable {
	static private int uniqueId = 0;
	protected int id; 
	protected String name; 
	protected String password; 
	
    public User(String name, String password) { 
        this.id       = ++uniqueId; 
        this.name     = name; 
        this.password = password; 
    } 
	 
    public int getId() { 
    	return id; 
    } 
    
    public String getName() { 
    	return name; 
	} 
    
    public abstract Role getRole(); 
 
    public boolean authenticate(String pw) { 
        return this.password.equals(pw); 
    } 
}
