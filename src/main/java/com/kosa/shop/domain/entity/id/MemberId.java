package com.kosa.shop.domain.entity.id;

import com.kosa.shop.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberId implements Serializable {

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;
}
