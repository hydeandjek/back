package com.example.demo.shareapi.dto.request;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalDateDTO {

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime approvalDate;


}
