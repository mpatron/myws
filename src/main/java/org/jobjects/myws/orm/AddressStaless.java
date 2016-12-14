package org.jobjects.myws.orm;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jobjects.myws.user.Address;
import org.jobjects.myws.user.User;

@Stateless
@Local({AddressFacade.class})
public class AddressStaless extends AbstractFacade<Address> implements AddressFacade {

	public AddressStaless() {
		super(Address.class);
	}
	
	@PersistenceContext(unitName = AppConstants.PERSISTENCE_UNIT_NAME)
	protected EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	 /**
   * @see org.jobjects.myws.orm.AddressFacade
   *      #findByNamedQuery(java.lang.String, java.lang.Object[])
   */
  public List<Address> findByFirstName(final User user) {
    TypedQuery<Address> query = getEntityManager().createNamedQuery(Address.FIND_BY_USER, Address.class);
    return query.setParameter("user", user).getResultList();
  }

}
 