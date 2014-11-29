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

}
