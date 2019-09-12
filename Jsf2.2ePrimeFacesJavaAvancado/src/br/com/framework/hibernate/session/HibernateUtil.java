package br.com.framework.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framework.implementacao.crud.VariavelConexaoUtil;

public class HibernateUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	
//	 uma conexão p sistema inteiro 
	private static SessionFactory factory = buildSessionFactory();

//	Lê o file de config hibernate.cfg.xml
	private static SessionFactory buildSessionFactory() {
		try {
			if (factory == null) {
				System.out.println("Levantando hibernate.cfg");
				factory = new Configuration().configure().buildSessionFactory();
			}
			return factory;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError(e.getMessage());
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return factory;
	}
	
//	Sessão hibernate
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}
	
//	abre uma nova session no SessionFactory
	public static Session openSession() {
		if (factory == null) {
			buildSessionFactory();
		}
		return factory.openSession();
	}
	
//	Hibernate encapsula o connection do JDBC. 
	public static Connection getConnectionProvider() throws SQLException{
		return ((SessionFactoryImplementor) factory).getConnectionProvider().getConnection();
	}
	
//	Obtendo connection do Tomcat
	public static Connection getConnection() throws Exception{
		InitialContext initialContext= new InitialContext();
		DataSource dataSource= (DataSource) initialContext.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_SOURCE);
		
		return dataSource.getConnection();
	}
	
//	pegando o JNDI
	public DataSource getDataSourceJNDI() throws NamingException{
		InitialContext initialContext = new InitialContext();
		return (DataSource) initialContext.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_SOURCE);
	}
 
}


























