package com.hhjang.springbatch.entity.storehistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface StoreHistoryRepository extends JpaRepository<StoreHistory, Long> {
}
