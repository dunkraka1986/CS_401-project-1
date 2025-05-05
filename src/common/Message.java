package common;
import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    protected final Type type;
    protected Status status;
    protected UserType userType;
    protected String text;
    protected ArrayList<String> list;

    public Message(){
        this.type = null;
        this.status = null;
        this.text = "Undefined";
    }
    
    public Message(Type type, Status status, UserType userType, String text) {
    	this.type = type;
    	this.status = status;
    	this.userType = userType;
    	this.text = text;
    }

    public Message(Type type, Status status, String text){
        this.type = type;
        this.status = status;
        this.text = text;
    }
    
    public Message(Type type, Status status, String text, ArrayList<String> list){
        this.type = type;
        this.status = status;
        this.text = text;
        this.list = list;
    }

    public Type getType(){
    	return type;
    }

    public Status getStatus(){
    	return status;
    }
    
    public UserType getUserType() {
    	return userType;
    }

    public String getText(){
    	return text;
    }
    
    public ArrayList<String> getList(){
    	return list;
    }

}



