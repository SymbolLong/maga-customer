package com.maga.customer.service;

import com.maga.customer.entity.Customer;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;

public interface CustomerService {

    Customer create(Customer customer);

    Customer update(Customer customer);

    Customer findById(Long id);

    Customer findByAccessKey(String accessKey);

    Page<Customer> findByName(String name, int page, int size);

    Customer lock(Customer customer);

    Customer unlock(Customer customer);

    Customer reset(Customer customer);

    JSONObject toJSONObject(Customer customer);

    JSONObject toJSONPage(Page<Customer> customers);

}
