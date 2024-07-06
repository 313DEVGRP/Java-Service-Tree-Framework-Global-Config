package com.arms.api.schedule.controller;

import com.arms.util.external_communicate.*;
import com.arms.util.external_communicate.vo.서버정보_엔티티;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;

import java.util.Calendar;

@Controller
public class ScheduleController {

    @Autowired
    private 엔진통신기 엔진통신기;

    @Autowired
    private 백엔드코어통신기 백엔드코어통신기;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/auth-sche/schedule/server_info_backup", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<서버정보_엔티티> 서버정보백업_스케줄러(ServerWebExchange exchange){

        logger.info("[ 암스스케쥴러 :: 서버정보백업_스케줄러 ] 동작 : {}", Calendar.getInstance().getTime());
        Iterable<서버정보_엔티티> 결과 = 엔진통신기.서버정보백업_스케줄러();
        return 결과;
    }

    @RequestMapping(value = "/auth-sche/schedule/jiraissue_index_backup", method = RequestMethod.GET)
    @ResponseBody
    public boolean 지라이슈_인덱스백업(ServerWebExchange exchange) {

        logger.info("[ 암스스케쥴러 :: 지라이슈_인덱스백업 ] 동작 : {}", Calendar.getInstance().getTime());
        boolean 결과 = 엔진통신기.지라이슈_인덱스백업();
        return 결과;
    }

    @RequestMapping(value = "/auth-sche/schedule/reqissue_es_store", method = RequestMethod.GET)
    @ResponseBody
    public String 각_제품서비스_별_요구사항이슈_조회_및_ES저장(ServerWebExchange exchange) {

        logger.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항이슈_조회_및_ES저장 ] 동작 : {}", Calendar.getInstance().getTime());
        String 결과 = 백엔드코어통신기.각_제품서비스_별_요구사항이슈_조회_및_ES저장();
        return 결과;
    }

    @RequestMapping(value = "/auth-sche/schedule/increment/reqissue_es_store", method = RequestMethod.GET)
    @ResponseBody
    public String 각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장(ServerWebExchange exchange) {
        long 시작시간 = System.currentTimeMillis();

        logger.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장 ] 동작 : {}", Calendar.getInstance().getTime());
        String 결과 = 백엔드코어통신기.각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장();

        long 종료시간 = System.currentTimeMillis();

        long 걸린시간 = 종료시간 - 시작시간;
        logger.info("스케줄러 호출이 걸린 시간: " + 걸린시간 + "밀리초");

        return 결과;
    }

    @RequestMapping(value = "/auth-sche/schedule/issue_es_load", method = RequestMethod.GET)
    @ResponseBody
    public String 각_제품서비스_별_요구사항_Status_업데이트_From_ES(ServerWebExchange exchange) {

        logger.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항_Status_업데이트_From_ES ] 동작 : {}", Calendar.getInstance().getTime());
        String 결과 = 백엔드코어통신기.각_제품서비스_별_요구사항_Status_업데이트_From_ES();
        return 결과;
    }

    @RequestMapping(value = "/auth-sche/schedule/recreate_failed_reqissue", method = RequestMethod.GET)
    @ResponseBody
    public String 각_제품서비스_별_생성실패한_ALM_요구사항_이슈_재생성(ServerWebExchange exchange) {

        logger.info("[ 암스스케쥴러 :: 각_제품서비스_별_생성실패한_ALM_요구사항_이슈_재생성 ] 동작 : {}", Calendar.getInstance().getTime());
        String 결과 = 백엔드코어통신기.각_제품서비스_별_생성실패한_ALM_요구사항_이슈_재생성();
        return 결과;
    }

    @Data
    public static class DTO{
        private String key;
        private String val;
    }

}
