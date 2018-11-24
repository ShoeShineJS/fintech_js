package lesson7.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SubscriptionInfo {
        private String id;
        private String instrumentId;
        private Double priceAlert;
        private String status;
        private String secName;
        private String ticker;
        private String secType;
        private String createdAt;
        private String completedAt;
        private String canceledAt;
}
