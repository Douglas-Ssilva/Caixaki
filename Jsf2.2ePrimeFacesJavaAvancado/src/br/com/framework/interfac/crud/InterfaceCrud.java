package br.com.framework.interfac.crud;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component //injeção de dependencia do Spring
@Transactional //Spring sabe que essa classe fará transação com banco
public interface InterfaceCrud<T> extends Serializable{

	void save(T t) throws Exception;
	void persist(T t) throws Exception;
	void saveOrUpdate(T t) throws Exception;
	void update(T t) throws Exception;
	void delete(T t) throws Exception;
	void executeUpdateDynamic(String query) throws Exception;
	Session getSession() throws Exception;
	void executeUpdateSQLDynamic(String query) throws Exception;
	void clearSession() throws Exception; //Limpando session hibernate
	void evict(Object o) throws Exception; //Retira o objeto da session hibernate
	T merge(T t) throws Exception;
	List<T> findList(Class<T> t) throws Exception;
	T findById(Class<T> t, Long id) throws Exception;
	List<T> findListByDynamicQuery(String query) throws Exception;
	List<?> getListSQLDynamic(String sql) throws Exception;
	List<Object[]> getListSQLDynamicArray(String sql) throws Exception;
	JdbcTemplate getJdbcTemplate();
	SimpleJdbcTemplate getJdbcTemplateSimple();
	SimpleJdbcInsert getJdbcTemplateSimpleInsert();
	Long amountRegisters(String table) throws Exception;
	Query getQuery(String query) throws Exception;
	List<T> findListQueryDynamic(String query, int initialRegister, int maxResults) throws Exception; //Carregamento por demanda
}



































