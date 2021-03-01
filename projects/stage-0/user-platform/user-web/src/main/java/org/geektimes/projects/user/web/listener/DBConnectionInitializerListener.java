package org.geektimes.projects.user.web.listener;

import org.geektimes.projects.user.sql.DBConnectionManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebListener
public class DBConnectionInitializerListener implements ServletContextListener {

    private Logger logger  = Logger.getLogger(DBConnectionInitializerListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("DBConnectionInitializerListener contextInitialized");
        cache(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBConnectionManager  dbConnectionManager = (DBConnectionManager) sce.getServletContext().getAttribute("dbConnectionManager");
        Connection connection =  dbConnectionManager.getConnection();
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cache(ServletContext context){
        // 获取JNDI数据源
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/UserPlatformDB");
            Connection connection =  ds.getConnection("","");
            DBConnectionManager dbConnectionManager = new DBConnectionManager();
            dbConnectionManager.setConnection(connection);
            context.setAttribute("dbConnectionManager",dbConnectionManager);
        } catch (Exception e) {
           logger.info("DBConnectionInitializerListener Exception:"+e.getCause());
        }
    }
}
