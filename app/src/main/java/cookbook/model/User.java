package cookbook.model;

import javafx.beans.binding.LongBinding;

public class User {
    private Long id;    
    private String name;
    private String userName;
    private String password;
    
    public User(Long id, String name, String userName, String password){
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setUnit(String password){
        this.password = password;
    }

    public String getName(){
        return name;
    }
    public String getUserName(){
        return userName;
    }
    public String getPasword(){
        return password;
    }
}
