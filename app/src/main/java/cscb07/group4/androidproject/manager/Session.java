package cscb07.group4.androidproject.manager;

public enum Session {
    FALL("Fall"), WINTER("Winter"), SUMMER("Summer");
    String name;
    Session(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
