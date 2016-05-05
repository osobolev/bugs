package common;

public class User {

    private int id;
    private Role role;
    private String name;

    public User(int id, Role role, String name) {
        this.id = id;
        this.role = role;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
