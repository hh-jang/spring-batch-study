package com.hhjang.springbatch.entity.store;

import com.hhjang.springbatch.entity.employee.Employee;
import com.hhjang.springbatch.entity.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository repository;

    @Transactional(readOnly = true)
    public long getProductSumAndCheckEmployeesQuery() {
        List<Store> stores = repository.findAll();
        long sumOfProductsPrice = stores.stream()
                .map(Store::getProducts)
                .flatMap(Collection::stream)
                .mapToLong(Product::getPrice)
                .sum();

        stores.stream()
                .map(Store::getEmployees)
                .flatMap(Collection::stream)
                .map(Employee::getName)
                .collect(Collectors.toList());

        return sumOfProductsPrice;
    }
}
