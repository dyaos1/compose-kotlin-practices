package com.example.membershipservice.adapter.out.persistence;

import com.example.membershipservice.domain.Membership;
import org.springframework.stereotype.Component;

@Component
public class MembershipMapper {
    public static Membership mapToDomainEntity(MembershipJpaEntity membershipEntity) {
        return Membership.generateMember(
                new Membership.MembershipId(membershipEntity.getMembershipId().toString()),
                new Membership.MembershipName(membershipEntity.getName()),
                new Membership.MembershipEmail(membershipEntity.getEmail()),
                new Membership.MembershipAddress(membershipEntity.getAddress()),
                new Membership.MembershipIsValid(membershipEntity.getIsValid()),
                new Membership.MembershipIsCorp(membershipEntity.getIsCorp())
        );
    }
}
