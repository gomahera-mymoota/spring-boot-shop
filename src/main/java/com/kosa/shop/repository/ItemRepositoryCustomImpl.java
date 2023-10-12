package com.kosa.shop.repository;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.domain.entity.Item;
import com.kosa.shop.domain.entity.QItem;
import com.kosa.shop.domain.entity.QItemImg;
import com.kosa.shop.dto.ItemSearchDto;
import com.kosa.shop.dto.MainItemDto;
import com.kosa.shop.dto.QMainItemDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.sellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        var dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
        dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("name", searchBy)) {
            return QItem.item.name.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    private BooleanExpression nameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.name.like("%" + searchQuery + "%");
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        var content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        var total = queryFactory
                .select(Wildcard.count)
                .from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        var item = QItem.item;
        var itemImg = QItemImg.itemImg;

        var content = queryFactory
                .select(new QMainItemDto(
                        item.id,
                        item.name,
                        item.detail,
                        itemImg.imgUrl,
                        item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.isRepImg.isTrue())
                .where(nameLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        var total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.isRepImg.isTrue(),
                        nameLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
