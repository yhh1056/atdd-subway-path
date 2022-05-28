package wooteco.subway.domain.path.fare.policy;

public class DefaultDiscountPolicy implements DiscountPolicy {

    private static final int MAX_AGE = 64;
    private static final int MIN_AGE = 19;

    @Override
    public int calculate(int fare) {
        return fare;
    }

    @Override
    public boolean isDiscountAge(int age) {
        return age >= MIN_AGE && age <= MAX_AGE;
    }
}
