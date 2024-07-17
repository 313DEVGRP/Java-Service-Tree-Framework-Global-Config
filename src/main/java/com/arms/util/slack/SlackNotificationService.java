package com.arms.util.slack;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SlackNotificationService {

    private static final String TOP_MESSAGE = "*[A-RMS] NOTIFICATION*";
    private static final String FOOTER_MESSAGE = "313devgrp";
    private static final String FOOTER_ICON = "http://www.313.co.kr/arms/img/313.png";

    private final SlackProperty slackProperty;
    private final Environment environment;


    public void sendMessageToChannel(final SlackProperty.Channel channel, final Exception e) {
        if(isStg()) {
            String title =
                    MessageFormat.format("[{0}] {1}", slackProperty.getProfile(), slackProperty.getServiceName());

            String message = messageInStackTrace(e);

            SlackMessageDTO slackMessageDTO = getSlackMessageMetaDTO(title);

            List<Attachment> attachments = List.of(slackMessageDTO.parseAttachment(message));

            Slack slack = Slack.getInstance();

            try {
                slack.methods(slackProperty.getToken()).chatPostMessage(
                        request -> request.channel(channel.name()).text(TOP_MESSAGE).attachments(attachments));
            } catch(Exception exception) {
                log.error("Failed to send Slack message: {}", exception.getMessage(), exception);
            }
        }
    }


    public void sendMessageToChannel(final SlackProperty.Channel channel, final String message) {
        if(isStg()) {
            String title =
                    MessageFormat.format("[{0}] {1}", slackProperty.getProfile(), slackProperty.getServiceName());

            SlackMessageDTO slackMessageDTO = getSlackMessageMetaDTO(title);

            List<Attachment> attachments = List.of(slackMessageDTO.parseAttachment(message));

            Slack slack = Slack.getInstance();

            try {
                slack.methods(slackProperty.getToken()).chatPostMessage(
                        request -> request.channel(channel.name()).text(TOP_MESSAGE).attachments(attachments));
            } catch(Exception exception) {
                log.error("Failed to send Slack message: {}", exception.getMessage(), exception);
            }
        }
    }


    private String messageInStackTrace(Exception e) {
        String filteredStackTrace = Arrays.stream(e.getStackTrace())
                .filter(stackTraceElement -> stackTraceElement.getClassName().contains("com.arms"))
                .map(StackTraceElement::toString).collect(Collectors.joining("\n", "\n\n[StackTrace]\n", ""));

        return e + (filteredStackTrace.isBlank() ? "" : filteredStackTrace);
    }


    private boolean isStg() {
        return Arrays.asList(environment.getActiveProfiles()).contains("stg");
    }


    private SlackMessageDTO getSlackMessageMetaDTO(String title) {
        return SlackMessageDTO.builder().title(title).footer(FOOTER_MESSAGE).footerIcon(FOOTER_ICON).build();
    }
}