
package com.github.learningtour786.kafka.tutorial.stream.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MsgId",
    "CreatedTime",
    "Sym",
    "BrokerID",
    "PublisherID",
    "ProductType",
    "UpdateType",
    "SupportStatus",
    "Bid",
    "Ask",
    "Mid"
})
public class MarketPrice {

    @JsonProperty("MsgId")
    private String msgId;
    @JsonProperty("CreatedTime")
    private Long createdTime;
    @JsonProperty("Sym")
    private String sym;
    @JsonProperty("BrokerID")
    private String brokerID;
    @JsonProperty("PublisherID")
    private String publisherID;
    @JsonProperty("ProductType")
    private String productType;
    @JsonProperty("UpdateType")
    private String updateType;
    @JsonProperty("SupportStatus")
    private Integer supportStatus;
    @JsonProperty("Bid")
    private Double bid;
    @JsonProperty("Ask")
    private Double ask;
    @JsonProperty("Mid")
    private Double mid;

    @JsonProperty("MsgId")
    public String getMsgId() {
        return msgId;
    }

    @JsonProperty("MsgId")
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @JsonProperty("CreatedTime")
    public Long getCreatedTime() {
        return createdTime;
    }

    @JsonProperty("CreatedTime")
    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    @JsonProperty("Sym")
    public String getSym() {
        return sym;
    }

    @JsonProperty("Sym")
    public void setSym(String sym) {
        this.sym = sym;
    }

    @JsonProperty("BrokerID")
    public String getBrokerID() {
        return brokerID;
    }

    @JsonProperty("BrokerID")
    public void setBrokerID(String brokerID) {
        this.brokerID = brokerID;
    }

    @JsonProperty("PublisherID")
    public String getPublisherID() {
        return publisherID;
    }

    @JsonProperty("PublisherID")
    public void setPublisherID(String publisherID) {
        this.publisherID = publisherID;
    }

    @JsonProperty("ProductType")
    public String getProductType() {
        return productType;
    }

    @JsonProperty("ProductType")
    public void setProductType(String productType) {
        this.productType = productType;
    }

    @JsonProperty("UpdateType")
    public String getUpdateType() {
        return updateType;
    }

    @JsonProperty("UpdateType")
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    @JsonProperty("SupportStatus")
    public Integer getSupportStatus() {
        return supportStatus;
    }

    @JsonProperty("SupportStatus")
    public void setSupportStatus(Integer supportStatus) {
        this.supportStatus = supportStatus;
    }

    @JsonProperty("Bid")
    public Double getBid() {
        return bid;
    }

    @JsonProperty("Bid")
    public void setBid(Double bid) {
        this.bid = bid;
    }

    @JsonProperty("Ask")
    public Double getAsk() {
        return ask;
    }

    @JsonProperty("Ask")
    public void setAsk(Double ask) {
        this.ask = ask;
    }

    @JsonProperty("Mid")
    public Double getMid() {
        return mid;
    }

    @JsonProperty("Mid")
    public void setMid(Double mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("msgId", msgId).append("createdTime", createdTime).append("sym", sym).append("brokerID", brokerID).append("publisherID", publisherID).append("productType", productType).append("updateType", updateType).append("supportStatus", supportStatus).append("bid", bid).append("ask", ask).append("mid", mid).toString();
    }

}
