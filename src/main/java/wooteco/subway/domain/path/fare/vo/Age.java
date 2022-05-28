package wooteco.subway.domain.path.fare.vo;

import java.util.Objects;

public class Age {

    private static final int MIN_AGE = 1;
    private static final int MAX_AGE = 150;

    private final int value;

    public Age(int value) {
        validateRange(value);
        this.value = value;
    }

    private void validateRange(int value) {
        if (MIN_AGE > value || value > MAX_AGE) {
            throw new IllegalArgumentException("나이는 1이상 150이하여야 합니다. value : " + value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Age)) {
            return false;
        }
        Age age = (Age) o;
        return value == age.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int getValue() {
        return value;
    }
}
