package com.arms.util.scheduler;

import com.arms.util.slack.SlackNotificationService;
import com.arms.util.slack.SlackProperty;
import com.arms.util.aspect.LogAndSlackNotify;
import com.arms.util.external_communicate.*;
import com.arms.util.external_communicate.vo.서버정보_엔티티;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Calendar;

@Slf4j
@Component
@RequiredArgsConstructor
public class 암스스케쥴러 {

    private final 내부통신기 내부통신기;

    private final 엔진통신기 엔진통신기;

    private final SlackNotificationService slackNotificationService;

    @LogAndSlackNotify
    @Scheduled(cron="0 0 1 * * *") // 매일 오전 01(새벽)시에
    @Async
    public void 서버정보백업_스케줄러() {
        Iterable<서버정보_엔티티> 결과 = 내부통신기.서버정보백업_스케줄러();
        String resultMessage = String.format("[ 암스스케쥴러 :: 서버정보백업_스케줄러 ] 결과 : %s", Calendar.getInstance().getTime()) + "\n\n" + 결과.toString();
        log.info(resultMessage);
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, resultMessage);
    }

    // @Scheduled(cron="0 30 1 * * *") // 매일 오전 01(새벽)시 30분에 실행
    // 엘라스틱 서치 롤링 인덱스를 사용 하기 때문에 자동 백업 중단 24.02.11
    @LogAndSlackNotify
    @Async
    public void 지라이슈_인덱스백업() {
        boolean 결과 = 내부통신기.지라이슈_인덱스백업();
        if(결과){
            log.info("지라이슈_인덱스백업 성공");
        }else{
            log.info("지라이슈_인덱스백업 실패");
        }
    }

    // 최초 한번 실행은 직접 실행으로 한다.
    @LogAndSlackNotify
    @Async
    public void 각_제품서비스_별_요구사항이슈_조회_및_ES저장() {
        String 결과 = 내부통신기.각_제품서비스_별_요구사항이슈_조회_및_ES저장();
        log.info(결과);
    }

    @LogAndSlackNotify
    @Scheduled(cron="0 0 3 * * *") // 매일 오전 03(새벽)시에 실행
    @Async
    public void 각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장() {
        String 결과 = 내부통신기.각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장();
        log.info(결과);
    }

    @LogAndSlackNotify
    @Scheduled(cron="0 0 6 * * *") // 매일 오전 06(새벽)시에 실행
    @Async
    public void 각_제품서비스_별_요구사항_Status_업데이트_From_ES() {
        String 결과 = 내부통신기.각_제품서비스_별_요구사항_Status_업데이트_From_ES();
        log.info(결과);
    }

    @Async
    @Scheduled(fixedDelay = 45000, initialDelay = 10000)
    public void 커넥션_상태_유지() {
        log.info("커넥션_상태_유지");
        엔진통신기.커넥션_상태_유지();
    }

    @LogAndSlackNotify
    @Scheduled(cron="0 0 9,12,15,18,21 * * *")// 매일 09, 12,시, 15시, 18시, 21시 동작
    @Async
    public void 각_제품서비스_별_생성실패한_ALM_요구사항_이슈_재생성() {
        String 결과 = 내부통신기.각_제품서비스_별_생성실패한_ALM_요구사항_이슈_재생성();
        log.info(결과);
    }
}
