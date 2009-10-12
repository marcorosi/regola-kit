package ${package}.dao.internal;

import ${package}.dao.LanguageDao;
import ${package}.model.Language;

import java.util.List;

import org.regola.dao.hibernate.HibernateGenericDao;
import org.springframework.stereotype.Repository;

@Repository("languageDao")
public class LanguageDaoHibernate extends HibernateGenericDao<Language, Integer>
		implements LanguageDao {

	public LanguageDaoHibernate() {
		super(Language.class);
	}

	public List<Language> readAll() {

		return getAll();
	}

}
