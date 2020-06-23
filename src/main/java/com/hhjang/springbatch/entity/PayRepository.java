package com.hhjang.springbatch.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayRepository extends JpaRepository<Pay, Long> {
    public List<Pay> findAllBySuccessStatus();
}
