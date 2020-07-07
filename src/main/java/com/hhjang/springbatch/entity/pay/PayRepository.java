package com.hhjang.springbatch.entity.pay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PayRepository extends JpaRepository<Pay, Long> {
    @Query("SELECT p FROM Pay p WHERE p.successStatus = true")
    List<Pay> findAllSuccess();
}
