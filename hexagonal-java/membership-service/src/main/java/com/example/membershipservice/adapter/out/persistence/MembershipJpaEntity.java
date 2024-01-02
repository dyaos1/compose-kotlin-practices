package com.example.membershipservice.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "membership")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembershipJpaEntity {
    @Id
    @GeneratedValue
    private Long membershipId;

    private String name;

    private String email;

    private String address;

    private Boolean isValid;

    private Boolean isCorp;

    public MembershipJpaEntity(String name, String email, String address, Boolean isValid, Boolean isCorp) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.isValid = isValid;
        this.isCorp = isCorp;
    }

    @Override
    public String toString() {
        return "MembershipJpaEntity{" +
                "membershipId=" + membershipId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", isValid=" + isValid +
                ", isCorp=" + isCorp +
                '}';
    }
}
