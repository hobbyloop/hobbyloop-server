package com.example.ticketservice.ticket.repository.centermembership;

import com.example.ticketservice.ticket.entity.CenterMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CenterMembershipRepository extends JpaRepository<CenterMembership, Long>, CenterMembershipRepositoryCustom {
    List<CenterMembership> findAllByCenterId(Long centerId);

    int countByCenterId(Long centerId);

    boolean existsByMemberId(Long memberId);

    CenterMembership findByMemberIdAndCenterId(Long memberId, Long centerId);
}
