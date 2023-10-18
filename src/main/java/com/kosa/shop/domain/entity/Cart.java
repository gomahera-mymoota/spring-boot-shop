package com.kosa.shop.domain.entity;

import com.kosa.shop.domain.entity.id.MemberId;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "carts")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Cart extends BaseEntity {

    @EmbeddedId
    private MemberId memberId;

    private Cart(Member member) {
        this.memberId = new MemberId(member);
    }

    public static Cart createCart(Member member) {
        return new Cart(member);
    }

    public void setMember(Member member) {
        this.memberId.setMember(member);
    }

    public Member getMember() {
        return this.memberId.getMember();
    }

    public Long getId() {
        return this.getMember().getId();
    }
}
