package com.example.dbook.payment.repository;

import com.example.dbook.payment.entity.BillingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillingKeyRepository extends JpaRepository<BillingKey, Long> {

    Optional<BillingKey> findByCustomerKey(String customerKey);

    Optional<BillingKey> findByMemberId(Long memberId);

    Optional<BillingKey> findByMemberEmail(String email);
}
