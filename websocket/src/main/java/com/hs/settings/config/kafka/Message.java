package com.hs.settings.config.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    private String id;

    @NotNull
    private Integer chatNo;

    @NotNull
    private String contentType;

    @NotNull
    private String content;

    private String senderName;

    private Integer senderNo;

    @NotNull
    private Integer saleNo;

    private long sendTime;
    private Integer readCount;
    private String senderEmail;

    public void setSendTimeAndSender(LocalDateTime sendTime, Integer senderNo, String senderName, Integer readCount) {
        this.senderName = senderName;
        this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.senderNo = senderNo;
        this.readCount = readCount;
    }
}