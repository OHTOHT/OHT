package template.travel.model;

public enum SeatClass {

    ECONOMY("Economy"),
    PREMIUM_ECONOMY("Premium Economy"),
    BUSINESS("Business"),
    FIRST_CLASS("First Class");

    private String displayName;

    SeatClass(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    // Optionally and/or additionally, toString.
    @Override
    public String toString() {
        return displayName;
    }

}
