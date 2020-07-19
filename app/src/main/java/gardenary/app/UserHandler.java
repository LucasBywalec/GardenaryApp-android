package gardenary.app;

import java.sql.Timestamp;

public class UserHandler {

    public String firstname;
    public String email;
    public Timestamp signUpDate;

    public UserHandler() {
    }

    public UserHandler(String firstname, String email, Timestamp signUpDate) {
        this.firstname = firstname;
        this.email = email;
        this.signUpDate = signUpDate;
    }

}
