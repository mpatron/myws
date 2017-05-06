package org.jobjects.myws.orm.user;
import org.jobjects.myws.rest.user.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Singleton
@Lock(LockType.READ)
public class SingletonUserRepository implements UserRepository {
    private Map<String, User> users;
    
    @PostConstruct
    void initialize() {
        users = new LinkedHashMap<String, User>();
    }

    @Override
    @Lock(LockType.WRITE)
    public void addUser(User user) {
        users.put(user.getEmail(), user);
    }
    
    @Override
    public Collection<User> getUsers() {
        return Collections.unmodifiableMap(users).values();
    }
    
    @Override
    public int getUserCount() {
        return users.size();
    }
    
    @Override
    public User getUser(String email) {
      return users.get(email);
    }

    @Override
    public User deleteUser(String email) {      
      return users.remove(email);
    }
}