package common;

public class Button {

    private String name;
    private String displayText;

    public Button(String name, String displayText) {
        this.name = name;
        this.displayText = displayText;
    }

    public String getName() {
        return name;
    }

    public String getDisplayText() {
        return displayText;
    }
}
