package allbugs;

import common.Role;
import common.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BugInFullList {

    private int id;
    private String text;
    private LocalDateTime createTime;
    private Status status;
    private int priority;
    private String name;
    private Role role;

    public BugInFullList(int id, String text, LocalDateTime createTime, Status status, int priority, String name, Role role) {
        this.id = id;
        this.text = text;
        this.createTime = createTime;
        this.status = status;
        this.priority = priority;
        this.name = name;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getCreateTime() {
        return createTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
    }

    public String getStatus() {
        return status.translate();
    }

    public int getPriority() {
        return priority;
    }


    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
