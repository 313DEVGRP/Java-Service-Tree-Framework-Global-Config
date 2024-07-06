package com.arms.util.external_communicate;

import com.arms.util.external_communicate.vo.서버정보_엔티티;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "engine-fire", url = "${arms.engine.url}")
public interface 엔진통신기 {

    @PostMapping("/engine/jira/0/issue/index/backup")
    boolean 지라이슈_인덱스백업();

    @PostMapping("/engine/serverinfo/backup/scheduler")
    Iterable<서버정보_엔티티> 서버정보백업_스케줄러();

    @GetMapping("/engine/connection/keep-alive")
    void 커넥션_상태_유지();
}
