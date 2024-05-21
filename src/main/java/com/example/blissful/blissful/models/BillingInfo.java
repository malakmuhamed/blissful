package com.example.blissful.blissful.models;
public class BillingInfo {
    private String fullName;
    private String email;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String nameOnCard;
    private String creditCardNumber;
    private String expMonth;
    private int expYear;
    private String cvv;

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getNameOnCard() { return nameOnCard; }
    public void setNameOnCard(String nameOnCard) { this.nameOnCard = nameOnCard; }

    public String getCreditCardNumber() { return creditCardNumber; }
    public void setCreditCardNumber(String creditCardNumber) { this.creditCardNumber = creditCardNumber; }

    public String getExpMonth() { return expMonth; }
    public void setExpMonth(String expMonth) { this.expMonth = expMonth; }

    public int getExpYear() { return expYear; }
    public void setExpYear(int expYear) { this.expYear = expYear; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
}
