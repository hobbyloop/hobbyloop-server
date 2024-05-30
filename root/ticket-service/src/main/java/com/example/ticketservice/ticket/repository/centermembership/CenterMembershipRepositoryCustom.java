package com.example.ticketservice.ticket.repository.centermembership;

import com.example.ticketservice.ticket.entity.CenterMembership;

import java.util.List;

public interface CenterMembershipRepositoryCustom {
    List<CenterMembership> getCenterMembershipListByCenterId(long centerId, int pageNo, int sortId);
}
