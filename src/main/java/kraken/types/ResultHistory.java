package kraken.types;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class ResultHistory {
    Map<String, ResultHistoryList> closed;
    int count;

    @JsonCreator
    public ResultHistory(
            @JsonProperty("trades") Map<String, ResultHistoryList> closed,
            @JsonProperty("count") int count) {
        this.closed = closed;
        this.count = count;
    }
}