package com.arms.util.aspect;

import com.arms.util.slack.SlackNotificationService;
import com.arms.util.slack.SlackProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAndSlackNotifyAspect {

    private final SlackNotificationService slackNotificationService;


    /**
     * LogAndSlackNotify 어노테이션이 붙은 메서드가 호출될 때 로그를 기록하고 슬랙으로 알림을 보내는 Aspect입니다.
     *
     * @author YourName
     * @since 1.0.0
     */
    @Around("@annotation(com.arms.util.aspect.LogAndSlackNotify)")
    public Object logAndSlackNotify(ProceedingJoinPoint joinPoint) throws Throwable {
        /**
         * 호출되는 메서드의 이름.
         */
        String methodName = joinPoint.getSignature().getName();

        /**
         * 메서드 실행 시작을 나타내는 포맷된 메시지.
         */
        String startMessage = String.format("[ 암스스케쥴러 :: %s ] 동작 시작 : %s", methodName, Calendar.getInstance().getTime());

        /**
         * 시작 메시지를 로그에 기록.
         */
        log.info(startMessage);

        /**
         * 시작 메시지를 지정된 슬랙 채널로 전송.
         */
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, startMessage);

        /**
         * Advice된 메서드 실행.
         */
        Object result = joinPoint.proceed();

        /**
         * 메서드 실행 종료를 나타내는 포맷된 메시지.
         */
        String endMessage = String.format("[ 암스스케쥴러 :: %s ] 동작 종료 : %s", methodName, Calendar.getInstance().getTime());
        /**
         * 종료 메시지를 로그에 기록.
         */
        log.info(endMessage);
        /**
         * 종료 메시지를 지정된 슬랙 채널로 전송.
         */
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, endMessage);

        /**
         * 메서드 실행 결과를 반환.
         *
         * @return 메서드 실행 결과.
         */
        log.info("[ LogAndSlackNotifyAspect ] : Method {} returned value : {}", methodName, result);
        return result;
    }
}