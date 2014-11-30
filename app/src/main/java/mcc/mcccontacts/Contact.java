package mcc.mcccontacts;

/**
 * Created by eduardocastellanosn on 11/29/14.
 */
public class Contact {

    private String sFirstName;
    private String sLastName;
    private String sId;

    public Contact(){
        this.sFirstName = "Cesar";
        this.sLastName = "Pereida";
    }

    public Contact(String name, String lastname)
    {
        this.sFirstName = name;
        this.sLastName = lastname;
        this.sId = "testID";
    }

    public String getName(){

        return this.sFirstName;
    }

    public String getId(){
        return this.sId;
    }

    public String getFullName(){
        return this.getName() + " " + this.getLastName();
    }

    public String getLastName(){
        return this.sLastName;
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

    public String toString()
    {
        return this.getFullName();
    }

}
