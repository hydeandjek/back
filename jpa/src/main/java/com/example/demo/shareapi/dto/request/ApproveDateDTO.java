package com.example.demo.shareapi.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveDateDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime approveDate;


}
