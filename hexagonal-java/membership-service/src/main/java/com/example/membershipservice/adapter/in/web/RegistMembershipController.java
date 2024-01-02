package com.example.membershipservice.adapter.in.web;

import com.example.membershipservice.application.port.in.RegisterMembershipCommand;
import com.example.membershipservice.application.port.in.RegisterMembershipUseCase;
import com.example.membershipservice.common.WebAdapter;
import com.example.membershipservice.domain.Membership;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class RegistMembershipController {
    private final RegisterMembershipUseCase registerMembershipUseCase;

    @PostMapping("/membership/register")
    Membership RegisterMembership(
        @RequestBody RegisterMembershipRequest request
    ) {
        // request
        // request -> command
        // usecase  --(request x, command o)

        RegisterMembershipCommand command = RegisterMembershipCommand.builder()
                .name(request.getName())
                .address(request.getAddress())
                .email(request.getEmail())
                .isValid(true)
                .isCorp(request.getIsCorp())
                .build();

        return registerMembershipUseCase.registerMembership(command);
    }
}
