package com.example.ticketservice.point.policy;

import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.PointHistory;

public interface PlatformPointPolicy {
    void calculate(Long totalAmount);
    PointHistory earnOrUse(Point point);
}
