package gardenary.app;

import java.sql.Timestamp;

public class UserHandler {

    public String firstname;
    public Timestamp signUpDate;

    public UserHandler() {
    }

    public UserHandler(String firstname, Timestamp signUpDate) {
        this.firstname = firstname;
        this.signUpDate = signUpDate;
    }

}
