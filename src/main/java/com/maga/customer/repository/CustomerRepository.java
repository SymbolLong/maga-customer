package com.maga.customer.repository;

import com.maga.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByAccessKey(String accessKey);

    Page<Customer> findByNameLike(String name, Pageable pageable);
}
