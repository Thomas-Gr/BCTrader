package kraken.types;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class ResultOpenOrder {
    Map<String, ResultClosedTradesList> closed;

    @JsonCreator
    public ResultOpenOrder(
            @JsonProperty("open") Map<String, ResultClosedTradesList> closed) {
        this.closed = closed;
    }
}