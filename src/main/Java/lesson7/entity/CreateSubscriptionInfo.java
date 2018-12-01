package lesson7.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateSubscriptionInfo {
    private String instrumentId;
    private String secName;
    private String secType;
    private Double priceAlert;

    public CreateSubscriptionInfo(String instrumentId, String secName, String secType, Double priceAlert) {
        this.instrumentId = instrumentId;
        this.secName = secName;
        this.secType = secType;
        this.priceAlert = priceAlert;
    }
}
