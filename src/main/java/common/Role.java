package common;

public enum Role {
    MANAGER, DEVELOPER, TESTER;
    public String translate() {
        switch (this) {
            case MANAGER: return "Менеджер";
            case DEVELOPER: return "Разработчик";
            case TESTER: return "Тестировщик";
        }
        return null;
    }
}
