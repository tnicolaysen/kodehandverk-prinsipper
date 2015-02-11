package solution;

import java.util.List;

abstract class Country {

    private String countryCode;
    private int callingCode;

    protected Country(String countryCode, int callingCode) {
        this.countryCode = countryCode;
        this.callingCode = callingCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getCallingCode() {
        return callingCode;
    }
}

class UnitedStates extends Country {

    public UnitedStates() {
        super("us", 1);
    }

}

class Norway extends Country {

    public Norway() {
        super("no", 47);
    }

}

class Sweden extends Country {

    public Sweden() {
        super("se", 46);
    }

}

public class CallingCodes {

    private List<Country> countries;

    public CallingCodes(List<Country> countries) {
        this.countries = countries;
    }

    public int getCallingCode(String countryCode) {
        return countries.stream()
                .filter(c -> c.getCountryCode().equals(countryCode))
                .map(Country::getCallingCode)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(countryCode));
    }

}
