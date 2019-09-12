package br.com.framework.implementacao.crud;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.interfac.crud.InterfaceCrud;

@Component
@Transactional
public class ImplementacaoCrud<T> implements InterfaceCrud<T>{

	private static final long serialVersionUID = 1L;
	private static SessionFactory sessionFactory= HibernateUtil.getSessionFactory();
	
	@Autowired //Injeção de dependencia do Spring
	private JdbcTemplateImpl jdbcTemplate; 
	
	@Autowired // Classes que ajudam a trabalhar com JDBC
	private SimpleJdbcTemplateImpl jdbcTemplateSimple;
	
	@Autowired
	private SimpleJdbcInsertImpl jdbcTemplateSimpleInsert;
	
	@Autowired
	private SimpleJdbcClassImpl jdbcTemplateSimpleClass;
	

	
//	Garatindo que a sessionFactory está criada
	private void validateTransaction() {
		if (!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction();
		}
	}
	
//	Em caso de não interceptação do filter, em requisições ajax, que seja commitado o processo
	private void commitProccessAjax() {
		sessionFactory.getCurrentSession().beginTransaction().commit();
	}
	
	private void rollbackProccessAjax() {
		sessionFactory.getCurrentSession().beginTransaction().rollback();
	}
	
	private void validateSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory= HibernateUtil.getSessionFactory();
		}
	}

	@Override
	public void save(T t) throws Exception {
		validateSessionFactory();
		sessionFactory.getCurrentSession().save(t);
		executeCleanInBD();
	}

	@Override
	public void persist(T t) throws Exception {
		validateSessionFactory();
		sessionFactory.getCurrentSession().persist(t);
		executeCleanInBD();
	}

	@Override
	public void saveOrUpdate(T t) throws Exception {
		validateSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdate(t);
		executeCleanInBD();
	}

	@Override
	public void update(T t) throws Exception {
		validateSessionFactory();
		sessionFactory.getCurrentSession().update(t);
		executeCleanInBD();
	}

	@Override
	public void delete(T t) throws Exception {
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(t);
		executeCleanInBD();
	}
	
	@Override
	public T merge(T t) throws Exception {
		validateSessionFactory();
		t = (T) sessionFactory.getCurrentSession().merge(t);
		executeCleanInBD();
		return t;
	}

	@Override
	public void executeUpdateDynamic(String query) throws Exception {
		validateSessionFactory();
		sessionFactory.getCurrentSession().createQuery(query).executeUpdate();
		executeCleanInBD();
	}

	@Override
	public Session getSession() throws Exception {
		validateSessionFactory();
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void executeUpdateSQLDynamic(String query) throws Exception {
		validateSessionFactory();
		sessionFactory.getCurrentSession().createSQLQuery(query).executeUpdate();
		executeCleanInBD();
	}

//	Limpando objetos da sessão
	@Override
	public void clearSession() throws Exception {
		sessionFactory.getCurrentSession().clear();
	}

//	Quando sei qual objeto está na sessão
	@Override
	public void evict(Object o) throws Exception {
		validateSessionFactory();
		sessionFactory.getCurrentSession().evict(o);
	}


	@Override
	public List<T> findList(Class<T> t) throws Exception {
		validateSessionFactory();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT(entity) from");
		sb.append(t.getSimpleName()).append(" entity ");
		return sessionFactory.getCurrentSession().createQuery(sb.toString()).list();
	}


	@Override
	public List<T> findListByDynamicQuery(String query) throws Exception {
		validateSessionFactory();
		List<T> list= new ArrayList<>();
		list= sessionFactory.getCurrentSession().createQuery(query).list();
		return list;
	}

	@Override
	public List<?> getListSQLDynamic(String sql) throws Exception {
		validateSessionFactory();
		List<?> list= sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return list;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	@Override
	public Long amountRegisters(String table) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(1) FROM ").append(table);		//Usando Spring. conta só pela primeira coluna. Obtendo mais performance
		return jdbcTemplate.queryForLong(sb.toString());
	}

//  Funcionará como uma table generica de consulta, com isso nao será preciso criar uma tela nova p cada cadastro
	@Override
	public Query getQuery(String query) throws Exception {
		validateSessionFactory();
		Query createQuery = sessionFactory.getCurrentSession().createQuery(query);
		return createQuery;
	}

//	Realiza consulta no banco, iniciando o carregamento a partir do registro passado via param -> initialRegister obtem o maximo de resultados passados em -> maxResults
	@Override
	public List<T> findListQueryDynamic(String query, int initialRegister, int maxResults) throws Exception {
		validateSessionFactory();
		List<T> list= new ArrayList<>();
		list= sessionFactory.getCurrentSession().createQuery(query).setFirstResult(initialRegister).setMaxResults(maxResults).list();
		return list;
	}

	@Override
	public T findById(Class<T> t, Long id) throws Exception {
		validateSessionFactory();
		return (T) sessionFactory.getCurrentSession().load(getClass(), id);
	}
	
	@Override
	public SimpleJdbcTemplate getJdbcTemplateSimple() {
		return jdbcTemplateSimple;
	}
	

	@Override
	public SimpleJdbcInsert getJdbcTemplateSimpleInsert() {
		return jdbcTemplateSimpleInsert;
	}

	public SimpleJdbcClassImpl getJdbcTemplateSimpleClass() {
		return jdbcTemplateSimpleClass;
	}
	
//	Quando mandando várias instruções p banco pode ser que ele se perca, executando de forma desordenada da que mandamos 
	private void executeCleanInBD() {
		sessionFactory.getCurrentSession().flush();
	}

//	Resolve o problema quando trás mais de um registro em uma sql dinamica
	@Override
	public List<Object[]> getListSQLDynamicArray(String sql) throws Exception {
		validateSessionFactory();
		List<Object[]> list= sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return list;
	}
}






















