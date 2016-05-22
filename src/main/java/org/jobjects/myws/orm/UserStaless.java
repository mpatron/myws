package org.jobjects.myws.orm;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jobjects.myws.user.User;

@Stateless
@Local({UserFacade.class})
public class UserStaless extends AbstractFacade<User> implements UserFacade {

	public UserStaless() {
		super(User.class);
	}
	
	@PersistenceContext(unitName = AppConstants.PERSISTENCE_UNIT_NAME)
	protected EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

}
 