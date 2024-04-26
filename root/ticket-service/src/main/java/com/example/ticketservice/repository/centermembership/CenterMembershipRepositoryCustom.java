package com.example.ticketservice.repository.centermembership;

import com.example.ticketservice.entity.CenterMembership;

import java.util.List;

public interface CenterMembershipRepositoryCustom {
    List<CenterMembership> getCenterMembershipListByCenterId(long centerId, int pageNo, int sortId);
}
