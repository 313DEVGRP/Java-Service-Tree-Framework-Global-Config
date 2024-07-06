package com.arms.util.external_communicate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "backend-core", url = "${arms.backend.url}")
public interface 백엔드코어통신기 {

    @GetMapping("/arms/scheduler/pdservice/reqstatus/loadToES")
    String 각_제품서비스_별_요구사항이슈_조회_및_ES저장();

    @GetMapping("/arms/scheduler/pdservice/reqstatus/increment/loadToES")
    String 각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장();

    @GetMapping("/arms/scheduler/pdservice/reqstatus/updateFromES")
    String 각_제품서비스_별_요구사항_Status_업데이트_From_ES();

    @GetMapping("/arms/scheduler/pdservice/reqstatus/recreateFailedReqIssue")
    String 각_제품서비스_별_생성실패한_ALM_요구사항_이슈_재생성();
}
