package org.chun.line.model;

public class LineBotEventDto {

  private String type; // message

  private LineBotMessageDto message;

  private Long timestamp;

  private LineBotUserDto source;

  private String replyToken;

  private String active;
}
