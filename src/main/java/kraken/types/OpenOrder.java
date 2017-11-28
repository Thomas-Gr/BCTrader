package kraken.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class OpenOrder {
    List<String> error;
    ResultOpenOrder result;

    @JsonCreator
    public OpenOrder(
            @JsonProperty("error") List<String> error,
            @JsonProperty("result") ResultOpenOrder result) {
        this.error = error;
        this.result = result;
    }
}