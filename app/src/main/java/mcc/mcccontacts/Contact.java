package mcc.mcccontacts;

import java.io.Serializable;

/**
 * Created by eduardocastellanosn on 11/29/14.
 */
public class Contact implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String mobile;
    private String address;
    private String id;

    public Contact(String firstName, String lastName, String email, String phoneNumber, String mobile, String address, String id){
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.mobile = mobile;
        this.address = address;
        this.id = id;
    }

    public String getName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getFullName(){
        return this.getName() + " " + this.getLastName();
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getMobileNumber(){
        return mobile;
    }

    public String getAddress(){
        return address;
    }

    public String getEmail(){
        return email;
    }

    public String getId(){
        return id;
    }

    public void setName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setMobileNumber(String mobile){
        this.mobile = mobile;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setId(String id){
        this.id = id;
    }
}
