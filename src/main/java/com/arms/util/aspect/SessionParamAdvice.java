package com.arms.util.aspect;

import com.arms.util.slack.SlackNotificationService;
import com.arms.util.slack.SlackProperty;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class SessionParamAdvice {

    private final SlackNotificationService slackNotificationService;
    private final String appName;

    public SessionParamAdvice(SlackNotificationService slackNotificationService
            , @Value("${spring.application.name}") String appName) {
        this.slackNotificationService = slackNotificationService;
        this.appName = appName;
    }

    @Around("execution(* com.arms..controller.*.*(..))")
    public Object sessionParam(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        ServerWebExchange exchange = (ServerWebExchange)Arrays.stream(args)
            .filter(a -> a instanceof ServerWebExchange)
            .findFirst()
            .orElse(null);

        String methodName
                = Arrays.stream(joinPoint.getSignature().toLongString().split(" ")).skip(1).collect(Collectors.joining(" "));
        try{
            Object proceed = joinPoint.proceed();
            return proceed;
        }catch (Exception e){
            slackNotificationService.sendMessageToChannel(SlackProperty.Channel.middleproxy, e);
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            String parameters = Arrays.stream(args).map(String::valueOf).collect(Collectors.joining("\t"));
            Optional.ofNullable(exchange)
                .ifPresentOrElse(a->{
                     a.getSession()
                        .flatMap(session -> {
                            log.error("{} Error 발생\tmethodName : {}\tsession    : {}\tparameter   : {}\terrorMsg    : {}",appName,methodName,session.getId(),parameters,errors);
                            return Mono.empty();
                        }).subscribe();
                },()->{
                    log.error("{} Error 발생\tmethodName : {}\tparameter   : {}\terrorMsg    : {}",appName,methodName,parameters,errors);
                });
            throw e;
        }
    }
}
