package com.example.ticketservice.point;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.point.dto.PointEarnedResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PointAcceptanceTest extends AcceptanceTest {

    Long memberId = 1L;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void earnSignupPointSuccess() throws Exception {
        // given & when
        PointEarnedResponseDto response = PointSteps.earnPointWhenJoining(memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getBalance()).isEqualTo(3000L);
    }
}
