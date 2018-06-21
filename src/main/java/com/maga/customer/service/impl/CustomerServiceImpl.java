package com.maga.customer.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.maga.customer.entity.Customer;
import com.maga.customer.repository.CustomerRepository;
import com.maga.customer.service.CustomerService;
import com.maga.customer.util.Md5Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer) {
        customer.setAccessKey(generateSecret());
        customer.setAccessSecret(generateSecret());
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer update(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.isPresent() ? customer.get() : null;
    }

    @Override
    public Customer findByAccessKey(String accessKey) {
        return customerRepository.findByAccessKey(accessKey);
    }

    @Override
    public Page<Customer> findByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        if (StringUtils.isEmpty(name)) {
            name = "%%";
        }else{
            name = "%"+name+"%";
        }
        return customerRepository.findByNameLike(name, pageRequest);
    }

    @Override
    public Customer lock(Customer customer) {
        customer.setLocked(true);
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer unlock(Customer customer) {
        customer.setLocked(false);
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer reset(Customer customer) {
        customer.setAccessKey(generateSecret());
        customer.setAccessSecret(generateSecret());
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public JSONObject toJSONObject(Customer customer) {
        JSONObject json = new JSONObject();
        json.put("id", customer.getId());
        json.put("locked", customer.isLocked());
        json.put("name", customer.getName());
        json.put("mobile", customer.getMobile());
        json.put("accessKey", customer.getAccessKey());
        json.put("accessSecret", customer.getAccessSecret());
        json.put("remark", customer.getRemark());
        return json;
    }

    @Override
    public JSONObject toJSONPage(Page<Customer> customers) {
        JSONArray array = new JSONArray();
        for (Customer customer : customers.getContent()) {
            array.add(toJSONObject(customer));
        }
        JSONObject json = new JSONObject();
        json.put("page", customers.getNumber() + 1);
        json.put("size", customers.getSize());
        json.put("total", customers.getTotalElements());
        json.put("customers", array);
        return json;
    }

    private String generateSecret() {
        return Md5Util.MD5(UUID.randomUUID().toString()).toUpperCase();
    }
}
