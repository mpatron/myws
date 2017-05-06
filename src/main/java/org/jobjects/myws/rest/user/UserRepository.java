package org.jobjects.myws.rest.user;
import org.jobjects.myws.orm.user.User;

import java.util.Collection;

import javax.ejb.Local;

@Local
public interface UserRepository {
    void addUser(User user);
    Collection<User> getUsers();
    int getUserCount();
    User getUser(String email);
    User deleteUser(String email);
}