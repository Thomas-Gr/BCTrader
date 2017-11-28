package kraken.interfaces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

import kraken.util.Calculator;

public class CalculatorTest {

    // Data from http://investexcel.net/how-to-calculate-macd-in-excel/
    private static final List<Double> values = ImmutableList.<Double>builder()
            .add(441.35).add(439.66).add(442.93).add(433.26).add(434.58).add(428.85)
            .add(443.86).add(454.74).add(452.97).add(456.77).add(463.84).add(458.66)
            .add(460.71).add(449.98).add(445.52).add(439.29).add(442.78).add(430.12)
            .add(417.2).add(408.38).add(405.46).add(406.13).add(398.67).add(390.53)
            .add(392.05).add(402.8).add(426.24).add(419.85).add(429.8).add(434.33)
            .add(435.69).add(426.98).add(426.21).add(423.2).add(427.72).add(431.99)
            .add(429.79).add(428.91).add(442.66).add(452.08).add(461.14).add(463.58)
            .add(461.91).add(452.73).add(452.08).add(454.49).add(455.72).add(443.66)
            .add(432.5).add(428.35).add(428.43).add(437.87).add(431.72).add(430.58)
            .add(425.66).add(431.14).add(420.05).add(430.47).add(441.4).add(444.57)
            .add(448.97).add(442.8).add(450.81).add(446.06).add(448.85).add(459.99)
            .build();

    @Test
    public void givenListOfValues_whenComputeMACD_thenReturnExpectedValue() {
        double macd = Calculator.computeMACD(values);

        assertThat(macd).isCloseTo(-1.198040081, within(0.001));
    }

}