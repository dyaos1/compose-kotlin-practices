package com.example.membershipservice.application.service;

import com.example.membershipservice.adapter.out.persistence.MembershipJpaEntity;
import com.example.membershipservice.adapter.out.persistence.MembershipMapper;
import com.example.membershipservice.application.port.in.RegisterMembershipCommand;
import com.example.membershipservice.application.port.in.RegisterMembershipUseCase;
import com.example.membershipservice.application.port.out.RegisterMembershipPort;
import com.example.membershipservice.domain.Membership;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class RegisterMembershipService implements RegisterMembershipUseCase {

    private final RegisterMembershipPort registerMembershipPort;
    private final MembershipMapper membershipMapper;
    @Override
    public Membership registerMembership(RegisterMembershipCommand command) {

        //??
        // command -> DB

        // biz logic -> DB
        // external system
        // port, adapter
        MembershipJpaEntity membershipEntity = registerMembershipPort.createMembership(
                new Membership.MembershipName(command.getName()),
                new Membership.MembershipEmail(command.getEmail()),
                new Membership.MembershipAddress(command.getAddress()),
                new Membership.MembershipIsValid(command.isValid()),
                new Membership.MembershipIsCorp(command.isCorp())
        );

        // entity -> membership domain

        return membershipMapper.mapToDomainEntity(membershipEntity);
    }
}
