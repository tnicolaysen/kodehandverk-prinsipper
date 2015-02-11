package solution;

class Membership {
    public double getDiscount(double amount) {
        return amount;
    }
}

class BronzeMembership extends Membership {
    public double getDiscount(double amount) {
        return super.getDiscount(amount) - 25;
    }
}

class SilverMembership extends Membership {
    public double getDiscount(double amount) {
        return super.getDiscount(amount) - 50;
    }
}

class GoldMembership extends Membership {
    public double getDiscount(double amount) {
        return super.getDiscount(amount) - 100;
    }
}