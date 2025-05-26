/* PIGMA - HARUSARI DDL 문서 */
/* 변경이력 관리 */
-- v.1.4 : 5/22 14시 30분까지 변경사항 모두 반영 완료

-- 사용할 데이터베이스 선택
use harusari;

-- 테이블 삭제
DROP TABLE IF EXISTS `alarm`;
DROP TABLE IF EXISTS `schedule`;
DROP TABLE IF EXISTS `automation_schedule`;
DROP TABLE IF EXISTS `diary`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `feedback`;
DROP TABLE IF EXISTS `member`;

-- member 테이블
CREATE TABLE `member`
(
    `member_id`             BIGINT                  NOT NULL AUTO_INCREMENT,
    `email`                 VARCHAR(255)            NOT NULL,
    `password`              VARCHAR(255)            NOT NULL,
    `nickname`              VARCHAR(255)            NOT NULL,
    `gender`                TINYINT(1)              NULL, -- 선택 안함 0, MALE 1, FEMALE 2
    `consent_personal_info` TINYINT(1)              NOT NULL DEFAULT 0,
    `auth_provider`         ENUM ('LOCAL', 'KAKAO') NOT NULL DEFAULT 'LOCAL',
    `user_registered_at`    TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_modified_at`      TIMESTAMP               NULL,
    `user_deleted_at`       TINYINT(1)                       DEFAULT 0,
    PRIMARY KEY (`member_id`)
);

-- alarm 테이블
CREATE TABLE `alarm`
(
    `alarm_id`      BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`     BIGINT       NOT NULL,
    `alarm_message` VARCHAR(255) NOT NULL,
    `is_read`       TINYINT(1)   NOT NULL DEFAULT 0,
    `type`          VARCHAR(50)  NOT NULL,
    `created_at`    TIMESTAMP    NOT NULL,
    CONSTRAINT `PK_ALARM` PRIMARY KEY (`alarm_id`),
    CONSTRAINT `FK_member_TO_alarm_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
);

ALTER TABLE alarm
    ADD COLUMN is_sent BIGINT DEFAULT 0;

-- category 테이블
CREATE TABLE `category`
(
    `category_id`       BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`         BIGINT       NOT NULL,
    `category_name`     VARCHAR(100) NOT NULL,
    `color`             VARCHAR(7)   NOT NULL DEFAULT '#111111',
    `completion_status` TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (`category_id`),
    FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
);

-- automation_schedule 테이블
CREATE TABLE `automation_schedule`
(
    `automation_schedule_id`      BIGINT                              NOT NULL AUTO_INCREMENT,
    `category_id`                 BIGINT                              NOT NULL,
    `automation_schedule_content` VARCHAR(100)                        NOT NULL,
    `end_date`                    TIMESTAMP                           NULL,
    `repeat_type`                 ENUM ('DAILY', 'WEEKLY', 'MONTHLY') NULL,
    `repeat_weekdays`             VARCHAR(50)                         NULL,
    `repeat_monthday`             INT                                 NULL,
    PRIMARY KEY (`automation_schedule_id`),
    FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
);

-- schedule 테이블
CREATE TABLE `schedule`
(
    `schedule_id`            BIGINT      NOT NULL AUTO_INCREMENT,
    `category_id`            BIGINT      NOT NULL,
    `automation_schedule_id` BIGINT      NULL,
    `schedule_content`       VARCHAR(50) NOT NULL,
    `schedule_date`          TIMESTAMP   NOT NULL,
    `created_at`             TIMESTAMP   NULL     DEFAULT CURRENT_TIMESTAMP,
    `modified_at`            TIMESTAMP   NULL,
    `completion_status`      TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (`schedule_id`),
    FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
    FOREIGN KEY (`automation_schedule_id`) REFERENCES `automation_schedule` (`automation_schedule_id`)
);

-- diary 테이블
CREATE TABLE `diary`
(
    `diary_id`      BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`     BIGINT       NOT NULL,
    `diary_title`   VARCHAR(100) NOT NULL,
    `diary_content` TEXT         NOT NULL,
    `created_at`    TIMESTAMP    NOT NULL,
    `modified_at`   TIMESTAMP    NULL,
    PRIMARY KEY (`diary_id`),
    FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
);


-- feedback 테이블
CREATE TABLE `feedback`
(
    `feedback_id`      BIGINT    NOT NULL AUTO_INCREMENT,
    `member_id`        BIGINT    NOT NULL,
    `feedback_content` TEXT      NULL,
    `feedback_date`    TIMESTAMP NULL,
    PRIMARY KEY (`feedback_id`),
    FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
);