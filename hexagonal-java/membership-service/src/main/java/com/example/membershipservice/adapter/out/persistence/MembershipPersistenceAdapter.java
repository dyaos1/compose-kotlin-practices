package com.example.membershipservice.adapter.out.persistence;

import com.example.membershipservice.application.port.out.RegisterMembershipPort;
import com.example.membershipservice.common.PersistenceAdapter;
import com.example.membershipservice.domain.Membership;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class MembershipPersistenceAdapter implements RegisterMembershipPort {
    private final SpringDataMembershipRepository membershipRepository;

    @Override
    public MembershipJpaEntity createMembership(
            Membership.MembershipName membershipName,
            Membership.MembershipEmail membershipEmail,
            Membership.MembershipAddress membershipAddress,
            Membership.MembershipIsValid membershipIsValid,
            Membership.MembershipIsCorp membershipIsCorp
    ) {
        return membershipRepository.save(
            new MembershipJpaEntity(
                membershipName.getNameValue(),
                membershipEmail.getEmailValue(),
                membershipAddress.getAddressValue(),
                membershipIsValid.getIsValidValue(),
                membershipIsCorp.getIsCorpValue()
            )
        );

    }
}
