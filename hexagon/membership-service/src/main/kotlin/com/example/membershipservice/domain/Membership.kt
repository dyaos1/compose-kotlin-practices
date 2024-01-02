package com.example.membershipservice.domain

import lombok.Value


class Membership(
    membershipId: String,
    name: String,
    email: String,
    address: String,
    isValid: Boolean,
    isCorp: Boolean,
) {
    var membershipId: String = ""
    var name: String = ""
    var email: String = ""
    var address: String = ""
    var isValid: Boolean = true
    var isCorp: Boolean = false


    fun generator(
        membershipId: MembershipId,
        name: Name,
        email: Email,
        address: Address,
        isValid: IsValid,
        isCorp: IsCorp,
    ): Membership {
        return Membership(
            membershipId.membershipId,
            name.name,
            email.email,
            address.address,
            isValid.isValid,
            isCorp.isCorp,
        )
    }

    class MembershipId(var membershipId: String)
    class Name(var name: String)
    class Email(var email: String)
    class Address(var address: String)
    class IsValid(var isValid: Boolean)
    class IsCorp(var isCorp: Boolean)
}