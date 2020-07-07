package com.hhjang.springbatch.entity.store;

import com.hhjang.springbatch.entity.employee.Employee;
import com.hhjang.springbatch.entity.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Store {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private String address;;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Store addProduct(Product product) {
        products.add(product);
        product.updateStore(this);
        return this;
    }

    public Store addEmployee(Employee employee) {
        employees.add(employee);
        return this;
    }
}
