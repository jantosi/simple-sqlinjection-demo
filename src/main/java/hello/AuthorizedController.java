package hello;

import hello.dao.PoorDao;
import hello.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.SQLException;

@Controller
public class AuthorizedController {

    @Autowired
    DataSource dataSource;

    @RequestMapping("/authorized")
    public ModelAndView greeting(HttpSession session) throws SQLException {
        ModelAndView model = new ModelAndView("authorized");
        model.addObject("userData", session.getAttribute("userData"));
        return model;
    }

    @RequestMapping(value = "/authorized", method = RequestMethod.POST)
    public ModelAndView saveDescription(@ModelAttribute("userData") UserData sentUserData, HttpSession session) throws SQLException {
        UserData userDataFromSession = (UserData) session.getAttribute("userData");
        userDataFromSession.setDescription(sentUserData.description);

        PoorDao.saveUserDescription(dataSource, userDataFromSession);

        ModelAndView model = new ModelAndView("authorized");
        model.addObject("userData", session.getAttribute("userData"));
        return model;
    }

}
