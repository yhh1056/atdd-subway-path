package wooteco.subway.domain.path.fare.policy;

public class FreeDiscountPolicy implements DiscountPolicy {

    private static final int FREE_AMOUNT = 0;
    private static final int MAX_AGE = 65;
    private static final int MIN_AGE  = 5;

    @Override
    public int calculate(int fare) {
        return FREE_AMOUNT;
    }

    @Override
    public boolean isDiscountAge(int age) {
        return age <= MIN_AGE || age >= MAX_AGE;
    }
}
