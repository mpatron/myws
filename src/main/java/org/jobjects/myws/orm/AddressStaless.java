package org.jobjects.myws.orm;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.jobjects.myws.user.Address;
import org.jobjects.myws.user.User;

/**
 * Classe de gestion des adresses.
 * @author Mickaël Patron
 * @version 2016-05-08
 *
 */
@Stateless
@Local({ AddressFacade.class })
public class AddressStaless extends AbstractFacade<Address>
    implements AddressFacade {
  /**
   * Le constructeur.
   */
  public AddressStaless() {
    super(Address.class);
  }

  /**
   * EntityManager de serveur J2EE.
   */
  @PersistenceContext(unitName = AppConstants.PERSISTENCE_UNIT_NAME)
  private EntityManager entityManager;

  /**
   * @param entityManager
   *          the entityManager to set
   */
  public final void setEntityManager(final EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /* (non-Javadoc)
   * @see org.jobjects.myws.orm.AbstractFacade#getEntityManager()
   */
  @Override
  public final EntityManager getEntityManager() {
    return entityManager;
  }

  /**
   * @see org.jobjects.myws.orm.AddressFacade
   *      #findByNamedQuery(java.lang.String, java.lang.Object[])
   */
  @Override
  public List<Address> findByFirstName(final User user) {
    TypedQuery<Address> query = getEntityManager()
        .createNamedQuery(Address.FIND_BY_USER, Address.class);
    return query.setParameter("user", user).getResultList();
  }
}
