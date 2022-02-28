-- activity_basic
CREATE TABLE ACTIVITY_BASIC
(
    ACT_NUM     bigserial PRIMARY KEY,
    USER_NUM    bigint       NOT NULL
        CONSTRAINT APP_USER_FK REFERENCES APP_USER,
    ACT_TITLE   varchar(40)  NOT NULL,
    ACT_DESC    varchar(512) NOT NULL,
    START_DATE  timestamp    NOT NULL,
    END_DATE    timestamp,
    JOIN_CODE   varchar(8)   NOT NULL,
    ACT_STATUS  char         NOT NULL,
    CREATE_DATE timestamp    NOT NULL DEFAULT NOW()
);
COMMENT ON TABLE ACTIVITY_BASIC IS '活動主檔';
COMMENT ON COLUMN ACTIVITY_BASIC.ACT_NUM IS '活動編號';
COMMENT ON COLUMN ACTIVITY_BASIC.USER_NUM IS '活動發起人編號';
COMMENT ON COLUMN ACTIVITY_BASIC.ACT_TITLE IS '活動標題';
COMMENT ON COLUMN ACTIVITY_BASIC.ACT_DESC IS '活動描述';
COMMENT ON COLUMN ACTIVITY_BASIC.START_DATE IS '活動開始時間';
COMMENT ON COLUMN ACTIVITY_BASIC.END_DATE IS '活動結束時間';
COMMENT ON COLUMN ACTIVITY_BASIC.JOIN_CODE IS '活動邀請碼';
COMMENT ON COLUMN ACTIVITY_BASIC.ACT_STATUS IS '活動狀態(0:準備中,1:進行中,9:結束)';
COMMENT ON COLUMN ACTIVITY_BASIC.CREATE_DATE IS '活動建立日期';

-- activity_dt
CREATE TABLE ACTIVITY_DT
(
    ACD_NUM       bigserial PRIMARY KEY,
    ACT_NUM       bigint      NOT NULL
        CONSTRAINT ACTIVITY_BASIC_FK REFERENCES ACTIVITY_BASIC,
    ACD_TITLE     varchar(40) NOT NULL,
    START_DATE    timestamp   NOT NULL DEFAULT NOW(),
    END_DATE      timestamp,
    COST          numeric(18, 3),
    PAY_TYPE      char        NOT NULL,
    PRE_PAID_USER bigint
        CONSTRAINT APP_USER_FK REFERENCES APP_USER
);
COMMENT ON TABLE ACTIVITY_DT IS '活動明細';
COMMENT ON COLUMN ACTIVITY_DT.ACD_NUM IS '活動明細編號';
COMMENT ON COLUMN ACTIVITY_DT.ACT_NUM IS '活動主檔編號';
COMMENT ON COLUMN ACTIVITY_DT.ACD_TITLE IS '活動明細標題';
COMMENT ON COLUMN ACTIVITY_DT.START_DATE IS '活動明細開始時間';
COMMENT ON COLUMN ACTIVITY_DT.END_DATE IS '活動明細結束時間';
COMMENT ON COLUMN ACTIVITY_DT.COST IS '活動明細花費';
COMMENT ON COLUMN ACTIVITY_DT.PAY_TYPE IS '活動明細拆帳方式';
COMMENT ON COLUMN ACTIVITY_DT.PRE_PAID_USER IS '活動明細預先付款人';

-- activity_payment
CREATE TABLE ACTIVITY_PAYMENT
(
    ACP_NUM  bigserial PRIMARY KEY,
    ACT_NUM  bigint         NOT NULL
        CONSTRAINT ACTIVITY_BASIC_FK REFERENCES ACTIVITY_BASIC,
    ACD_NUM  bigint         NOT NULL
        CONSTRAINT ACTIVITY_DT_FK REFERENCES ACTIVITY_DT,
    USER_NUM bigint         NOT NULL
        CONSTRAINT APP_USER_FK REFERENCES APP_USER,
    PRICE    numeric(18, 3) NOT NULL,
    IS_PAID  char           NOT NULL,
    PAY_DATE timestamp
);
COMMENT ON TABLE ACTIVITY_PAYMENT IS '活動收支紀錄檔';
COMMENT ON COLUMN ACTIVITY_PAYMENT.ACP_NUM IS '活動收支編號';
COMMENT ON COLUMN ACTIVITY_PAYMENT.ACT_NUM IS '活動主檔編號';
COMMENT ON COLUMN ACTIVITY_PAYMENT.ACD_NUM IS '活動明細編號';
COMMENT ON COLUMN ACTIVITY_PAYMENT.USER_NUM IS '使用者編號';
COMMENT ON COLUMN ACTIVITY_PAYMENT.PRICE IS '收支金額';
COMMENT ON COLUMN ACTIVITY_PAYMENT.IS_PAID IS '是否付款';
COMMENT ON COLUMN ACTIVITY_PAYMENT.PAY_DATE IS '付款日期';

-- activity_set
CREATE TABLE ACTIVITY_SET
(
    ACS_NUM    bigserial PRIMARY KEY,
    ACT_NUM    bigint    NOT NULL
        CONSTRAINT ACTIVITY_BASIC_FK REFERENCES ACTIVITY_BASIC,
    USER_NUM   bigint    NOT NULL
        CONSTRAINT APP_USER_FK REFERENCES APP_USER,
    STATUS     char      NOT NULL,
    START_DATE timestamp NOT NULL DEFAULT NOW(),
    END_DATE   timestamp
);
COMMENT ON TABLE ACTIVITY_SET IS '活動分群記錄檔';
COMMENT ON COLUMN ACTIVITY_SET.ACS_NUM IS '活動分群編號';
COMMENT ON COLUMN ACTIVITY_SET.ACT_NUM IS '活動主檔編號';
COMMENT ON COLUMN ACTIVITY_SET.USER_NUM IS '使用者編號';
COMMENT ON COLUMN ACTIVITY_SET.STATUS IS '使用者狀態(0:邀請中,1:參加中,2:拒絕邀請,9:已離開)';
COMMENT ON COLUMN ACTIVITY_SET.START_DATE IS '開始時間';
COMMENT ON COLUMN ACTIVITY_SET.END_DATE IS '結束時間';

-- activity_dt_exclude
CREATE TABLE ACTIVITY_DT_EXCLUDE
(
    ACE_NUM  bigserial PRIMARY KEY,
    ACD_NUM  bigint NOT NULL
        CONSTRAINT ACTIVITY_DT_FK REFERENCES ACTIVITY_DT,
    USER_NUM bigint NOT NULL
        CONSTRAINT APP_USER_FK REFERENCES APP_USER,
    REASON   varchar(256)
);
COMMENT ON TABLE ACTIVITY_DT_EXCLUDE IS '活動分帳排除紀錄';
COMMENT ON COLUMN ACTIVITY_DT_EXCLUDE.ACE_NUM IS '活動分帳排除編號';
COMMENT ON COLUMN ACTIVITY_DT_EXCLUDE.ACD_NUM IS '活動明細編號';
COMMENT ON COLUMN ACTIVITY_DT_EXCLUDE.USER_NUM IS '排除使用者編號';
COMMENT ON COLUMN ACTIVITY_DT_EXCLUDE.REASON IS '排除原因';




