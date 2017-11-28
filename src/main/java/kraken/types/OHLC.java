package kraken.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class OHLC {
    long time;
    double open;
    double high;
    double low;
    double close;
    double vwap;
    double volume;
    long count;

    @JsonCreator
    public OHLC(
            @JsonProperty("time") long time,
            @JsonProperty("open") double open,
            @JsonProperty("high") double high,
            @JsonProperty("low") double low,
            @JsonProperty("close") double close,
            @JsonProperty("vwap") double vwap,
            @JsonProperty("volume") double volume,
            @JsonProperty("count") long count) {
        this.time = time;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.vwap = vwap;
        this.volume = volume;
        this.count = count;
    }
}