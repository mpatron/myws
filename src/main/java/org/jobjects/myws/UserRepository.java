package org.jobjects.myws;
import java.util.List;
import javax.ejb.Local;

@Local
public interface UserRepository {
    void addOrder(List<String> order);
    List<List<String>> getOrders();
    int getOrderCount();
}