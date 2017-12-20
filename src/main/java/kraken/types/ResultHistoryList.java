package kraken.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class ResultHistoryList {
    private final String ordertxid;
    private final String pair;
    private final String time;
    private final String type;
    private final String ordertype;
    private final String price;
    private final String cost;
    private final String fee;
    private final String vol;
    private final String margin;
    private final String misc;

    @JsonCreator
    public ResultHistoryList(
             @JsonProperty("ordertxid") String ordertxid,
             @JsonProperty("pair") String pair,
             @JsonProperty("time") String time,
             @JsonProperty("type") String type,
             @JsonProperty("ordertype") String ordertype,
             @JsonProperty("price") String price,
             @JsonProperty("cost") String cost,
             @JsonProperty("fee") String fee,
             @JsonProperty("vol") String vol,
             @JsonProperty("margin") String margin,
             @JsonProperty("misc") String misc) {
        this.ordertxid = ordertxid;
        this.pair = pair;
        this.time = time;
        this.type = type;
        this.ordertype = ordertype;
        this.price = price;
        this.cost = cost;
        this.fee = fee;
        this.vol = vol;
        this.margin = margin;
        this.misc = misc;
    }
}