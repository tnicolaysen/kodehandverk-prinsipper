package example;

public class Membership {

    private int membershipType;

    public Membership(int membershipType) {
        this.membershipType = membershipType;
    }

    public double getDiscount(double amount) {
        if (membershipType == 1) {
            return amount - 100;
        } else {
            return amount;
        }
    }

}