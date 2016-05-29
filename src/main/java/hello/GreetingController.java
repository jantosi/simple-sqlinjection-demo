package hello;

import hello.dao.PoorDao;
import hello.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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

    @RequestMapping("/")
    public String greeting(Model model) throws SQLException {
        model.addAttribute("userData", new UserData(0, "Andrzej","haslo",null));

        getUsersAndPrint();

        return "greeting";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public String greetingLogin(final UserData userData, HttpSession session) throws SQLException {
        System.out.println();
        System.out.println("User tries to log in:");
        System.out.println(userData.toString());
        System.out.println();

        UserData u = PoorDao.getUserData(dataSource, userData, true);

        if (u != null) {
            System.out.println("\nSUCCESSFUL LOGIN!" + u);
            session.setAttribute("userData", u);
            return "redirect:/authorized";
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
            String description = resultSet.getString("description");

            UserData u = new UserData(id, login, password, description);
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
