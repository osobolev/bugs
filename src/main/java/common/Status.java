package common;

public enum Status {
    /**
     * Статус сразу после создания задачи
     */
    OPENED,
    /**
     * Статус после начала работы разработчика над задачей
     */
    ASSIGNED,
    /**
     * Статус после окончания работы разработчика над задачей (над задачами в этом статусе работают тестировщики)
     */
    RESOLVED,
    /**
     * Статус после окончания тестирования, если баг не исправлен (после этого задача снова должна быть передана в разработку)
     */
    REOPENED,
    /**
     * Статус после окончания тестирования, если баг исправлен
     */
    FIXED;

    public String translate() {
        switch (this) {
            case OPENED: return "Открыт";
            case ASSIGNED: return "Назначен";
            case REOPENED: return "Переоткрыт";
            case RESOLVED: return "Исправлен";
            case FIXED: return "Закрыт";
        }
        return null;
    }
}
