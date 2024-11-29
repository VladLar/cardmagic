package org.vlad;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {
    private Long pan;
    private String type;
    private Integer amount;
    private String currencyCode;
    private String status;

    @JsonCreator
    public Transaction(@JsonProperty("pan") Long pan,
                       @JsonProperty("type") String type,
                       @JsonProperty("amount") Integer amount,
                       @JsonProperty("currency") String currencyCode,
                       @JsonProperty("status") String status)
    {
        this.pan = pan;
        this.type = type;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.status = status;
    }

    public Long getPan()
    {
        return pan;
    }

    public void setPan(Long pan)
    {
        this.pan = pan;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Integer getAmount()
    {
        return amount;
    }

    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String toString()
    {
        StringBuilder out = new StringBuilder();

        out.append("{\"data\":{\"transaction\":{");

        out.append(String.format("\"pan\":\"%s\",", Utils.sanitizePAN(pan)));
        out.append(String.format("\"type\":\"%s\",", type));
        out.append(String.format("\"amount\":\"%s\",", amount));
        out.append(String.format("\"currency\":\"%s\",", currencyCode));
        out.append(String.format("\"status\":\"%s\"", status));

        out.append("}}}");

        return out.toString();
    }
}
