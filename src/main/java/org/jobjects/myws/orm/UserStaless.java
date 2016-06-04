package org.jobjects.myws.orm;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

	 /**
   * @see be.bzbit.framework.domain.repository.GenericRepository
   *      #findByNamedQuery(java.lang.String, java.lang.Object[])
   */
  public List<User> findByFirstName(final String firstName) {
    TypedQuery<User> query = getEntityManager().createNamedQuery(User.FIND_BY_FIRSTNAME, User.class);
    return query.setParameter("firstName", firstName).getResultList();
  }
  
  public User findByEmail(final String email) {
    User returnValue=null;
    List<User> users = findByNamedQuery(User.FIND_BY_EMAIL, email);
    for (User user : users) {
      returnValue = user;
      break;
    }
    return returnValue;
  }
  
}
 