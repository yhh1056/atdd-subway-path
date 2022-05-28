package wooteco.subway.domain.path.fare.policy;

import java.util.Arrays;

public enum AgeDiscountPolicy {
    CHILDREN(new ChildrenDiscountPolicy()),
    TEENAGER(new TeenagerDiscountPolicy()),
    DEFAULT(new DefaultDiscountPolicy()),
    FREE(new FreeDiscountPolicy());

    private final DiscountPolicy discountPolicy;

    AgeDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public static AgeDiscountPolicy find(int age) {
        return Arrays.stream(AgeDiscountPolicy.values())
                .filter(it -> it.discountPolicy.isDiscountAge(age))
                .findFirst()
                .orElse(DEFAULT);
    }

    public int calculate(int fare) {
        return discountPolicy.calculate(fare);
    }
}
