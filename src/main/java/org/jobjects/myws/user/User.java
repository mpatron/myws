package org.jobjects.myws.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.jobjects.myws.orm.AbstractUUIDBaseEntity;

@Entity
@NamedQueries({
  @NamedQuery(name = User.FIND_BY_FIRSTNAME, query = "select t from User t where t.firstName = :firstName"),
  @NamedQuery(name = User.FIND_BY_EMAIL, query = "select t from User t where t.email = ?1") })
public class User extends AbstractUUIDBaseEntity implements Serializable {
  
  public final static String FIND_BY_FIRSTNAME = "User.findByFirstName";
  public final static String FIND_BY_EMAIL = "User.findByEmail";
  
  /**
   * 
   */
  private static final long serialVersionUID = -598324264166203579L;

  public User() {
  }

  @Size(min = 2, max = 20, message = "La longueur du prénom est comprise entre 2 et 20 caractères.")
  @NotNull
  private String firstName;

  @Size(max = 20, message = "La longueur du nom est inférieur à 20 caractères.")
  private String lastName;

  @Size(max = 320)
  @NotNull
  @Pattern(regexp = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$")
  private String email;

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName
   *          the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName
   *          the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email
   *          the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("User [firstName=");
    builder.append(firstName);
    builder.append(", lastName=");
    builder.append(lastName);
    builder.append(", email=");
    builder.append(email);
    builder.append("]");
    return builder.toString();
  }

}
