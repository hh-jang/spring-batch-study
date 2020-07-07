package com.hhjang.springbatch.entity.employee;

import com.hhjang.springbatch.entity.store.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Employee {
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private Long id;
    private String name;
    private LocalDate hireDate;

    @ManyToOne
    @JoinColumn(name = "store_id", foreignKey = @ForeignKey(name = "FK_EMPLOYEE_STORE"))
    private Store store;

    public Employee(String name, LocalDate hireDate) {
        this.name = name;
        this.hireDate = hireDate;
    }

    public void updateStore(Store store) {
        this.store = store;
    }
}
