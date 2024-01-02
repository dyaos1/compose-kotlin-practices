package com.example.membershipservice.application.port.in;

import com.example.membershipservice.common.UseCase;
import com.example.membershipservice.domain.Membership;

@UseCase
public interface RegisterMembershipUseCase {
    Membership registerMembership(RegisterMembershipCommand command);
}
