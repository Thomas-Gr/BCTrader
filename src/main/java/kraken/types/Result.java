package kraken.types;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class Result {
    List<OHLC> numbers;
    String last;

    @JsonCreator
    public Result(
            @JsonProperty("numbers") List<List<String>> numbers,
            @JsonProperty("last") String last) {
        this.numbers = transform(numbers);
        this.last = last;
    }

    private List<OHLC> transform(List<List<String>> numbers) {
        return numbers.stream()
                .map(a -> new OHLC(
                        Long.valueOf(a.get(0)),
                        Double.valueOf(a.get(1)),
                        Double.valueOf(a.get(2)),
                        Double.valueOf(a.get(3)),
                        Double.valueOf(a.get(4)),
                        Double.valueOf(a.get(5)),
                        Double.valueOf(a.get(6)),
                        Long.valueOf(a.get(7))))
                .collect(toList());

    }
}