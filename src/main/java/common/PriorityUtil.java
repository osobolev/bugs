package common;

public class PriorityUtil {

    public static String priorityToString(int priority) {
        switch (priority) {
            case 0: return "Низкий";
            case 1: return "Нормальный";
            case 2: return "Высокий";
        }
        throw new IllegalStateException();
    }
}
