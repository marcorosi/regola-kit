package org.regola.finder;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

public class FinderIntroductionAdvisor extends DefaultIntroductionAdvisor {

	private static final long serialVersionUID = -292340212437223013L;

	public FinderIntroductionAdvisor() {
		super(new FinderIntroductionInterceptor());
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean matches(Class clazz) {
		return FinderExecutor.class.isAssignableFrom(clazz);
	}
}
