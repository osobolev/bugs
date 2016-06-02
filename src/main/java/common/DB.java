package common;

public class DB {

//    public static final String DB_NAME = "jdbc:h2:file:/F:/bugs";
    public static final String DB_NAME = "jdbc:h2:~/bugs";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
