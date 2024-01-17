package com.example.membershipservice.application.port.in;

import com.example.membershipservice.domain.Membership;

public interface RegisterMembershipUseCase {
    Membership registerMembership(RegisterMembershipCommand command);
}
