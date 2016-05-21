package hello;

import hello.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GreetingController {

    @Autowired
    DataSource dataSource;

    @RequestMapping("/greeting")
    public String greeting(Model model) throws SQLException {
        model.addAttribute("userData", new UserData(0, "andrzejikuba","haslo"));

        getUsersAndPrint();

        return "greeting";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/greeting")
    public String greetingLogin(final UserData userData) throws SQLException {
        System.out.println();
        System.out.println("User tries to log in:");
        System.out.println(userData.toString());
        System.out.println();

        Connection connection = DataSourceUtils.getConnection(dataSource);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                String.format("SELECT * FROM UserData WHERE login='%s' AND password='%s';", userData.login, userData.password));

        if(resultSet.next()){
            System.out.println("\nSUCCESSFUL LOGIN!" + userData);
        } else {
            System.out.println("\nUNSUCCESSFUL LOGIN!" + userData);
        }

        getUsersAndPrint();

        return "greeting";
    }

    private List<UserData> getUsers() throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM UserData");

        List<UserData> users = new ArrayList<>();
        while(resultSet.next()) {
            int id = resultSet.getInt("id");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");

            UserData u = new UserData(id, login, password);
            users.add(u);
        }
        connection.close();

        return users;
    }

    private void getUsersAndPrint() throws SQLException {
        System.out.println("\nFetching users.");
        List<UserData> users = getUsers();
        users.stream().forEach(System.out::println);
        System.out.println();
    }

}
