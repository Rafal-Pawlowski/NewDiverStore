package com.ralf.NewDiverStore.order.service;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.order.domain.model.Order;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
@Getter
public class SessionOrderService {


    private Order order;
    private Customer customer;

    public Order initiateOrderSession() {
        if (this.order == null) {
            this.order = new Order();
        }

        if (this.customer == null) {
            this.customer = new Customer();
            customer.addOrder(this.order);
        }
        return this.order;
}


}
