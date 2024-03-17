package com.threestar.selectstar.dto.meeting.response;

import com.threestar.selectstar.entity.Meeting;
import com.threestar.selectstar.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class GetNotificationPageResponse {
    String content;
    String url;
    public static GetNotificationPageResponse fromEntity(Notification notification) {
        return GetNotificationPageResponse.builder()
                .content(notification.getNotification_content())
                .url(notification.getNotification_url())
                .build();
    }
}
