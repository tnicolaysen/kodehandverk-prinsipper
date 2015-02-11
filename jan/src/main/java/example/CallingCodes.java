package example;

public class CallingCodes {

    public int getCallingCode(String countryCode) {
        switch (countryCode) {
            case "us":
                return 1;
            case "no":
                return 47;
            case "se":
                return 46;
            default:
                throw new IllegalStateException(countryCode);
        }
    }

}
