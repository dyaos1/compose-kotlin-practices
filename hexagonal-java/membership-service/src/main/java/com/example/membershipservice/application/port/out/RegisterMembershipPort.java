package com.example.membershipservice.application.port.out;

import com.example.membershipservice.adapter.out.persistence.MembershipJpaEntity;
import com.example.membershipservice.domain.Membership;

public interface RegisterMembershipPort {
    MembershipJpaEntity createMembership(
        Membership.MembershipName membershipName,
        Membership.MembershipEmail membershipEmail,
        Membership.MembershipAddress membershipAddress,
        Membership.MembershipIsValid membershipIsValid,
        Membership.MembershipIsCorp membershipIsCorp
    );
}
