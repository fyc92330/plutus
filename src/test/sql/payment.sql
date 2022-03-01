-- payment_record
CREATE TABLE PAYMENT_RECORD
(
    PAYMENT_NUM   bigserial PRIMARY KEY,
    USER_NUM      bigint                  NOT NULL
        CONSTRAINT APP_USER_FK REFERENCES APP_USER,
    PAYMENT_TITLE varchar(40)             NOT NULL,
    PAYMENT_DESC  varchar(512),
    PAYMENT_COST  numeric(18, 3)          NOT NULL,
    RECORD_TYPE   char                    NOT NULL,
    RECORD_DATE   timestamp               NOT NULL,
    CREATE_DATE   timestamp DEFAULT NOW() NOT NULL,
    UPDATE_DATE   timestamp
);
COMMENT ON TABLE PAYMENT_RECORD IS '財務記錄檔';
COMMENT ON COLUMN PAYMENT_RECORD.PAYMENT_NUM IS '財務紀錄編號';
COMMENT ON COLUMN PAYMENT_RECORD.USER_NUM IS '財務紀錄所有人編號';
COMMENT ON COLUMN PAYMENT_RECORD.PAYMENT_TITLE IS '財務紀錄標題';
COMMENT ON COLUMN PAYMENT_RECORD.PAYMENT_DESC IS '財務紀錄描述';
COMMENT ON COLUMN PAYMENT_RECORD.PAYMENT_COST IS '財務紀錄金額';
COMMENT ON COLUMN PAYMENT_RECORD.RECORD_TYPE IS '財務紀錄類型(1:收入,2:支出)';
COMMENT ON COLUMN PAYMENT_RECORD.RECORD_DATE IS '財務紀錄發生日期';
COMMENT ON COLUMN PAYMENT_RECORD.CREATE_DATE IS '財務紀錄建立日期';
COMMENT ON COLUMN PAYMENT_RECORD.UPDATE_DATE IS '財務紀錄修改日期';

-- payment_tag
CREATE TABLE PAYMENT_TAG
(
    TAG_NUM       bigserial PRIMARY KEY,
    TAG_GROUP_NUM bigint                                         NOT NULL
        CONSTRAINT PAYMENT_TAG_GROUP_FK REFERENCES PAYMENT_TAG_GROUP,
    TAG_NAME      varchar(20)                                    NOT NULL,
    TAG_COLOR     varchar(6) DEFAULT 'ffffff'::character varying NOT NULL
);
COMMENT ON TABLE PAYMENT_TAG IS '財務紀錄標籤';
COMMENT ON COLUMN PAYMENT_TAG.TAG_NUM IS '標籤編號';
COMMENT ON COLUMN PAYMENT_TAG.TAG_GROUP_NUM IS '標籤所屬群編號';
COMMENT ON COLUMN PAYMENT_TAG.TAG_NAME IS '標籤名稱';
COMMENT ON COLUMN PAYMENT_TAG.TAG_COLOR IS '標籤顏色';

-- payment_tag_group
CREATE TABLE PAYMENT_TAG_GROUP
(
    TAG_GROUP_NUM   bigserial PRIMARY KEY,
    TAG_GROUP_NAME  varchar(20)                                    NOT NULL,
    TAG_GROUP_COLOR varchar(6) DEFAULT 'ffffff'::character varying NOT NULL,
    USER_NUM        bigint                                         NOT NULL
        CONSTRAINT APP_USER_PK REFERENCES APP_USER
);
COMMENT ON TABLE PAYMENT_TAG_GROUP IS '財務紀錄標籤群';
COMMENT ON COLUMN PAYMENT_TAG_GROUP.TAG_GROUP_NUM IS '標籤群編號';
COMMENT ON COLUMN PAYMENT_TAG_GROUP.TAG_GROUP_NAME IS '標籤群名稱';
COMMENT ON COLUMN PAYMENT_TAG_GROUP.TAG_GROUP_COLOR IS '標籤群顏色';
COMMENT ON COLUMN PAYMENT_TAG_GROUP.USER_NUM IS '標籤群所有人編號';

-- payment_record_m_tag
CREATE TABLE PAYMENT_RECORD_M_TAG
(
    PRMT_NUM      bigserial PRIMARY KEY,
    PAYMENT_NUM   bigint NOT NULL
        CONSTRAINT PAYMENT_RECORD_FK REFERENCES PAYMENT_RECORD,
    TAG_GROUP_NUM bigint NOT NULL
        CONSTRAINT PAYMENT_TAG_GROUP_FK REFERENCES PAYMENT_TAG_GROUP,
    TAG_NUM       bigint NOT NULL
        CONSTRAINT PAYMENT_TAG_FK REFERENCES PAYMENT_TAG
);
COMMENT ON TABLE PAYMENT_RECORD_M_TAG IS '財務紀錄標籤關聯檔';
COMMENT ON COLUMN PAYMENT_RECORD_M_TAG.PRMT_NUM IS '財務紀錄標籤關聯編號';
COMMENT ON COLUMN PAYMENT_RECORD_M_TAG.PAYMENT_NUM IS '財務紀錄編號';
COMMENT ON COLUMN PAYMENT_RECORD_M_TAG.TAG_GROUP_NUM IS '標籤群編號';
COMMENT ON COLUMN PAYMENT_RECORD_M_TAG.TAG_NUM IS '標籤編號';