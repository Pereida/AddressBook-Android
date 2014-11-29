package mcc.mcccontacts;

/**
 * Created by eduardocastellanosn on 11/29/14.
 */
public class Contact {
    public Contact(){

    }

    public String getName(){

        return "TestName";
    }

    public String getFullName(){
        return this.getName() + " " + this.getLastName();
    }

    public String getLastName(){
        return "TestLastName";
    }

    public String getPhoneNumber(){
        return "+52 474-332-323";
    }

    public String getMobileNumber(){
        return "+358 23-323-444";
    }

    public String getAddress(){
        return "Jamerantaival 5, Espoo";
    }

    public String getEmail(){
        return "test@testemail.com";
    }



}
