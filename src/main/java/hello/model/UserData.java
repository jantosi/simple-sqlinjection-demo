package hello.model;

/**
 * Created by antosikj (Jakub Antosik) on 21/05/16.
 */
public class UserData {
    public String login;
    public String password;
    public int id;

    public UserData(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public UserData() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
