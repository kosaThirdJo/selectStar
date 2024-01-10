package com.threestar.selectstar.dto.user.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.threestar.selectstar.domain.entity.User;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserRequest {
	private String name;
	private String password;

}
