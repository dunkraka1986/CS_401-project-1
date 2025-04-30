import java.io.Serializable;

enum Role {STUDENT, ADMIN}

public abstract class User implements Serializable {
	protected String id; 
	protected String name; 
	protected String password; 
	
    public User(String id, String name, String password) { 
        this.id       = id; 
        this.name     = name; 
        this.password = password; 
    } 
	 
    public String getId() { 
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
