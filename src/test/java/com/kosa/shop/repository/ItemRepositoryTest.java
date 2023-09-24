package com.kosa.shop.repository;

import com.kosa.shop.constant.ItemSellStatus;
import com.kosa.shop.entity.Item;
import com.kosa.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    static List<Item> items = new ArrayList<>();

    @BeforeAll
    static void prepareItems() {
        for (int i = 1; i <= 10; i++) {
            var item = new Item();
            item.setName("테스트 상품" + i);
            item.setDetail("테스트 상품 상세 설명" + i);
            item.setPrice(10000 + i);
            item.setSellStatus(i <= 5 ? ItemSellStatus.SELL : ItemSellStatus.SOLD_OUT);
            item.setStockNumber(i <= 5 ? 100 : 0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            items.add(item);
        }
    }

    @AfterEach
    void deleteItemList() {
        itemRepository.deleteAll();
    }

    void given() {
        for (var item : items) {
            itemRepository.save(item);
        };

        em.flush();
        em.clear();
    }

    @Order(10)
    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        // given: 준비
        var expectedItem = new Item();
        expectedItem.setName("테스트 상품");
        expectedItem.setPrice(10000);
        expectedItem.setDetail("테스트 상품 상세 설명");
        expectedItem.setSellStatus(ItemSellStatus.SELL);
        expectedItem.setStockNumber(100);
        expectedItem.setRegTime(LocalDateTime.now());
        expectedItem.setUpdateTime(LocalDateTime.now());

        // Repository에 저장하기 전에는 id가 정해지지 않음
        System.out.println("\n==>>> Before saving: " + expectedItem.getId()+ "\n");

        // when: 실행
        var savedItem = itemRepository.save(expectedItem);
        // Repository에 저장한 후 @GeneratedValue에 의해 id가 결정됨
        System.out.println("\n==>>> After saving: " + expectedItem.getId() + "\n");

        em.flush();
        em.clear();

        // then: 검증 - 수동, 자동 중 자동 검증 권장
        var actualItem = itemRepository.findByName(expectedItem.getName());      // Query Method
        // item 필드를 모두 검증하는 대신 읽은 아이템 개수와 id 값이 같은 것으로 비교해도 충분
        assertThat(actualItem.size()).isEqualTo(1);
        assertThat(actualItem.get(0).getName()).isEqualTo(expectedItem.getName());

        // equals(), hashCode() 오버러이드 하지 않았을 경우 아래처럼 비교
//        assertThat(savedItem).usingRecursiveComparison()
//                .isEqualTo(item);
    }

    @Order(20)
    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByNameTest() {
        // given
        given();

        // when
        var itemList = itemRepository.findByName("테스트 상품3");

        // then
        var expectedItem = items.get(2);
        var actualItem = itemList.get(0);

        assertThat(actualItem.getId()).isEqualTo(expectedItem.getId());
    }

    @Order(30)
    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByNameOrDetailTest() {
        // given
        given();

        // when
        var actualList = itemRepository.findByNameOrDetail("테스트 상품1", "테스트 상품 상세 설명5");

        // then
        var actualNames = new ArrayList<String>();
        for (var item : actualList) {
            actualNames.add(item.getName());
        }
        var expectedNames = new String[]{items.get(0).getName(), items.get(4).getName()};

        assertThat(actualNames).contains(expectedNames);
    }

    @Order(40)
    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByDetailTest() {
        // given
        given();

        // when
        var actualList = itemRepository.findByDetail("테스트 상품 상세 설명");

        // then
        assertThat(actualList.size()).isEqualTo(10);
    }

    @Order(50)
    @Test
    @DisplayName("상품 Querydsl 조회 테스트 1")
    public void queryDslTest() {
        // given
        given();

        // when
        var queryFactory = new JPAQueryFactory(em);
        var qItem = QItem.item;
        var query = queryFactory.selectFrom(qItem)
                .where(qItem.sellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.detail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        var actualList = query.fetch();

        // then
        assertThat(actualList.size()).isEqualTo(5);
    }

    @Order(60)
    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2() {
        // given
        given();

        // when
        var booleanBuilder = new BooleanBuilder();
        var item = QItem.item;
        var itemDetail = "테스트 상품 상세 설명";
        var price = 10003;
        var itemSellStat = "SELL";  // 외부 입력이 ItemSellStatus 열거형이 아니라 문자열인 경우가 대부분이므로

        booleanBuilder.and(item.detail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.sellStatus.eq(ItemSellStatus.SELL));
        }

        var pageable = PageRequest.of(0, 5);
        var itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);

        // then
        assertThat(itemPagingResult.getTotalElements()).isEqualTo(2);

        var actualList = itemPagingResult.getContent();
        var actualNames = new ArrayList<String>();
        for (var actualItem : actualList) {
            actualNames.add(actualItem.getName());
        }
        var expectedNames = new String[]{items.get(3).getName(), items.get(4).getName()};

        assertThat(actualNames).contains(expectedNames);
    }
}