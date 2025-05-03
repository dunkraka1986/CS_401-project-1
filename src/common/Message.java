package common;

import java.io.Serializable;

public class Message implements Serializable {
    protected final Type type;
    protected Status status;
    protected String text;

    public Message(){
        this.type = null;
        this.status = null;
        this.text = "Undefined";
    }

    public Message(Type type, Status status, String text){
        this.type = type;
        this.status = status;
        this.text = text;
    }
    
    private void setStatus(Status status){
    	this.status = status;
    }

    private void setText(String text){
    	this.text = text;
    }

    public Type getType(){
    	return type;
    }

    public Status getStatus(){
    	return status;
    }

    public String getText(){
    	return text;
    }

}

