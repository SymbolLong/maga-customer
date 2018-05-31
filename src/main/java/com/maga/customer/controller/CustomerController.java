package com.maga.customer.controller;

import com.maga.customer.entity.ApiResult;
import com.maga.customer.entity.Customer;
import com.maga.customer.service.CustomerService;
import com.maga.customer.util.ApiResultBuilder;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ApiResult create(@RequestParam String name, @RequestParam String mobile, @RequestParam String remark) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setMobile(mobile);
        customer.setRemark(remark);
        customerService.create(customer);
        return ApiResultBuilder.success("创建账户成功", customerService.toJSONObject(customer));
    }

    @PutMapping("/{id}")
    public ApiResult update(@PathVariable Long id, @RequestParam String name, @RequestParam String mobile) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return ApiResultBuilder.failure("账户不存在");
        }
        customer.setName(name);
        customer.setMobile(mobile);
        customerService.update(customer);
        return ApiResultBuilder.success("修改账户成功", customerService.toJSONObject(customer));
    }

    @GetMapping("/{id}")
    public ApiResult findById(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return ApiResultBuilder.failure("账户不存在");
        }
        return ApiResultBuilder.success("查询账户成功", customerService.toJSONObject(customer));
    }

    @GetMapping("/accessKey")
    public ApiResult findByAccessKey(@RequestParam String accessKey) {
        Customer customer = customerService.findByAccessKey(accessKey);
        if (customer == null) {
            return ApiResultBuilder.failure("账户标志错误");
        }
        return ApiResultBuilder.success("查询账户成功", customerService.toJSONObject(customer));
    }

    @GetMapping("/list")
    public ApiResult findByName(@RequestParam String name, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Customer> customers = customerService.findByName(name, page, size);
        return ApiResultBuilder.success("查询成功", customerService.toJSONPage(customers));
    }

    @GetMapping("/lock/{id}")
    public ApiResult lock(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return ApiResultBuilder.failure("账户不存在");
        }
        customerService.lock(customer);
        return ApiResultBuilder.success("账户锁定成功");
    }

    @GetMapping("/unlock/{id}")
    public ApiResult unlock(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return ApiResultBuilder.failure("账户不存在");
        }
        customerService.unlock(customer);
        return ApiResultBuilder.success("账户解锁成功");
    }

    @GetMapping("/reset/{id}")
    public ApiResult reset(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return ApiResultBuilder.failure("账户不存在");
        }
        customerService.reset(customer);
        return ApiResultBuilder.success("重置密钥成功", customerService.toJSONObject(customer));
    }

}
