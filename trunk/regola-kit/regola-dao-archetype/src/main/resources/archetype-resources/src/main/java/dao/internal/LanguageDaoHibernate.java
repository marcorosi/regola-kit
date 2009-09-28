package ${package}.dao.internal;

import ${package}.dao.ProductDao;
import ${package}.model.Language;

import java.util.List;

import org.regola.dao.hibernate.HibernateGenericDao;
import org.springframework.stereotype.Repository;

@Repository("languageDao")
public class LanguageDaoHibernate extends HibernateGenericDao<Language, Integer>
		implements ProductDao {

	public ProductDaoHibernate() {
		super(Language.class);
	}

	public List<Language> readAll() {

		return getAll();
	}

}
