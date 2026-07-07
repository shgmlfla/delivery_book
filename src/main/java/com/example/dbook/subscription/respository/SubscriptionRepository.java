package com.example.dbook.subscription.respository;

import com.example.dbook.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    //최신 구독권 해제(ACTIVE 상태)
    Optional<Subscription> findFirstByMemberIdOrderByStartDateDesc(Long memberId);

    @Query("SELECT s FROM Subscription s JOIN FETCH s.member WHERE s.nextChargeDate = :date AND s.status = :status")
    List<Subscription> findTargetSubscriptions(@Param("date") LocalDate date, @Param("status") Subscription.SubscriptionStatus status);
}
