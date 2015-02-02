package spms.listeners;

// Apache DBCP 적용
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import spms.dao.MemberDao;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
  BasicDataSource ds; // Datasource 구현체(commons-dbcp.jar)에서 사용
    
  @Override
  public void contextInitialized(ServletContextEvent event) {
    try {
      ServletContext sc = event.getServletContext();
      
      ds = new BasicDataSource();
      ds.setDriverClassName(sc.getInitParameter("driver"));
      ds.setUrl(sc.getInitParameter("url"));
      ds.setUsername(sc.getInitParameter("username"));
      ds.setPassword(sc.getInitParameter("password"));
      
      // 서블릿 컨테이너 구현체(서버)에서 설정한 값을 가져와서 사용
      InitialContext initialContext = new InitialContext();
      DataSource ds = (DataSource)initialContext.lookup(
    		  "java:comp/env/jdbc/studydb");
      
      // java:comp/env		: 응용 프로그램 환경 항목
      // java:comp/env/jdbc	: JDBC 데이터 소스
      // java:comp/env/mail	: JavaMail 연결 객체
      // java:comp/env/url	: URL 정보
      // java:comp/env/jms	: JMS 연결 객체
      // java:comp/ejb		: EJB 컴포넌트
      // java:comp/UserTransaction : UserTransaction 객체
      	
      
      
      MemberDao memberDao = new MemberDao();
      memberDao.setDataSource(ds);
      
      sc.setAttribute("memberDao", memberDao);

    } catch(Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    try { if (ds != null) ds.close(); } catch (SQLException e) {}
  }
}
