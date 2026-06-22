package com.example.dbook.subscription.respository;

import com.example.dbook.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    //최신 구독권 해제(ACTIVE 상태)
    Optional<Subscription> findFirstByMemberIdOrderByStartDateDesc(Long memberId);
}
