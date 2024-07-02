package com.example.ticketservice.point.policy;

import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.Points;
import com.example.ticketservice.point.entity.PointHistory;
import org.springframework.data.util.Pair;

public interface PlatformPointPolicy {
    void calculate(Long totalAmount);
    Pair<Point, PointHistory> earn(Points points);
}
