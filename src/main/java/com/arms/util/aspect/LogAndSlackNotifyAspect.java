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


    @Around("@annotation(com.arms.util.aspect.LogAndSlackNotify)")
    public Object logAndSlackNotify(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String startMessage = String.format("[ 암스스케쥴러 :: %s ] 동작 시작 : %s", methodName, Calendar.getInstance().getTime());
        log.info(startMessage);
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, startMessage);

        Object result = joinPoint.proceed();

        String endMessage = String.format("[ 암스스케쥴러 :: %s ] 동작 종료 : %s", methodName, Calendar.getInstance().getTime());
        log.info(endMessage);
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, endMessage);

        return result;
    }
}