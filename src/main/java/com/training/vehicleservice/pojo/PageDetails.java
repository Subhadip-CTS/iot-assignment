package com.training.vehicleservice.pojo;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageDetails {

    Long totalNumberOfRecord;
    Integer totalNumberOfPage;
    Integer currentPage;
}
