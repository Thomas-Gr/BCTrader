package kraken.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class History {
    List<String> error;
    ResultHistory result;

    @JsonCreator
    public History(
            @JsonProperty("error") List<String> error,
            @JsonProperty("result") ResultHistory result) {
        this.error = error;
        this.result = result;
    }
}