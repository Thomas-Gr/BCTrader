package kraken.types;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class ResultClosedTradesList {
    String refid;
    String refcond;
    String userref;
    String status;
    String reason;
    String opentm;
    String closetm;
    String starttm;
    String expiretm;
    Map<String, String> descr;
    String vol;
    String vol_exec;
    String cost;
    String fee;
    String stopprice;
    String limitprice;
    String price;
    String misc;
    String oflags;

    @JsonCreator
    public ResultClosedTradesList(
            @JsonProperty("refid") String refid,
            @JsonProperty("refcond") String refcond,
            @JsonProperty("userref") String userref,
            @JsonProperty("status") String status,
            @JsonProperty("reason") String reason,
            @JsonProperty("opentm") String opentm,
            @JsonProperty("closetm") String closetm,
            @JsonProperty("starttm") String starttm,
            @JsonProperty("expiretm") String expiretm,
            @JsonProperty("descr") Map<String, String> descr,
            @JsonProperty("vol") String vol,
            @JsonProperty("vol_exec") String vol_exec,
            @JsonProperty("cost") String cost,
            @JsonProperty("fee") String fee,
            @JsonProperty("stopprice") String stopprice,
            @JsonProperty("limitprice") String limitprice,
            @JsonProperty("price") String price,
            @JsonProperty("misc") String misc,
            @JsonProperty("oflags") String oflags) {
        this.refid = refid;
        this.refcond = refcond;
        this.userref = userref;
        this.status = status;
        this.reason = reason;
        this.opentm = opentm;
        this.closetm = closetm;
        this.starttm = starttm;
        this.expiretm = expiretm;
        this.descr = descr;
        this.vol = vol;
        this.vol_exec = vol_exec;
        this.cost = cost;
        this.fee = fee;
        this.stopprice = stopprice;
        this.limitprice = limitprice;
        this.price = price;
        this.misc = misc;
        this.oflags = oflags;
    }
}