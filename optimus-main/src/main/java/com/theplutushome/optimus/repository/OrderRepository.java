package com.theplutushome.optimus.repository;

import com.theplutushome.optimus.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, String> {
}
