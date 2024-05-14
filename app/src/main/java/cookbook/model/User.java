package cookbook.model;

public class User {
    private Long id;    
    private String name;
    private String userName;
    private String password;
    private int isAdmin;
    
    public User(Long id, String name, String userName, String password, int isAdmin){
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
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

    public void setIsAdmin(int isAdmin){
        this.isAdmin = isAdmin;
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
    public int getIsAdmin(){
        return isAdmin;
    }
}
