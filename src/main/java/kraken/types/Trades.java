package kraken.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class Trades {
    List<String> error;
    Result result;

    @JsonCreator
    public Trades(
            @JsonProperty("error") List<String> error,
            @JsonProperty("result") Result result) {
        this.error = error;
        this.result = result;
    }

}
