package com.arms.util.external_communicate.vo;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class 서버정보_엔티티 {

    private Long connectId;

    private String type;

    private String userId;

    private String passwordOrToken;

    private String uri;
}
