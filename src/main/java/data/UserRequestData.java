package data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequestData extends MainPOJO {
    private String email;
    private String password;
    private String name;

    public UserRequestData(){
    }
    public UserRequestData(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public UserRequestData(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Request{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
