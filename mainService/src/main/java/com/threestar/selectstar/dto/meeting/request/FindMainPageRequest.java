package com.threestar.selectstar.dto.meeting.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class FindMainPageRequest {
    private Integer page;
    private Integer size;
    private String order;
    private Integer category;
    private String criteria;
}
