package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.sql.DBConnectionManager;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.logging.Logger;

@Path("/register")
public class RegisterController implements PageController {

    private final static Logger logger = Logger.getLogger(RegisterController.class.getName());

    @Path("/user")
    @POST
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        DBConnectionManager dbConnectionManager = (DBConnectionManager) request.getServletContext().getAttribute("dbConnectionManager");
        Connection connection =  dbConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        String sql=  " INSERT INTO users(name,password,email,phoneNumber) VALUES ";
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
        Enumeration<String> attrs = request.getAttributeNames();
        while (attrs.hasMoreElements()){
            for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                 String name =  propertyDescriptor.getName();
                logger.info("filed name i "+name);
            }
        }
        StringBuffer sb = new StringBuffer(sql);
        statement.execute(sb.toString());
        return "success.jsp";
    }
}
