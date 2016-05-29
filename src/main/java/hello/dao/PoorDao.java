package hello.dao;

import hello.model.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by antosikj (Jakub Antosik) on 29/05/16.
 */
public class PoorDao {
    private static final Logger log = LoggerFactory.getLogger(PoorDao.class);

    public static UserData getUserData(DataSource dataSource, UserData userData, boolean forAuth){
        UserData u = null;

        try(Connection connection = DataSourceUtils.getConnection(dataSource)) {
            Statement statement = connection.createStatement();
            String sql = forAuth
                    ? String.format("SELECT * FROM UserData WHERE login='%s' AND password='%s';", userData.login, userData.password)
                    : String.format("SELECT * FROM UserData WHERE login='%s';", userData.login);

            ResultSet resultSet = statement.executeQuery(sql);

            if(resultSet.next()) {
                u = new UserData(resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getString("description"));
            }
        } catch(SQLException e) {
            log.error("getUserData", e);
        }

        return u;
    }

    public static void saveUserDescription(DataSource dataSource, UserData userData) {
        try(Connection connection = DataSourceUtils.getConnection(dataSource)){
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate(
                    String.format("UPDATE UserData SET description='%s' WHERE login='%s';", userData.description, userData.login)
            );

            log.info(String.format("Updated %d rows.", i));

        } catch (SQLException e) {
            log.error("saveUserDescription", e);
        }
    }
}
