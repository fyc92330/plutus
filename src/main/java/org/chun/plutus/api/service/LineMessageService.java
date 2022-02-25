package org.chun.plutus.api.service;

import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MemberJoinedEvent;
import com.linecorp.bot.model.event.MessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LineMessageService {

  public void handleLineCallbackRequest(CallbackRequest request){

    for(Event lineEvent : request.getEvents()){

      if(lineEvent instanceof MessageEvent){
        System.out.println("MESSAGE");

      }else if(lineEvent instanceof JoinEvent){
        System.out.println("JOIN");

      }else if(lineEvent instanceof FollowEvent){
        System.out.println("FOLLOW");

      }else if(lineEvent instanceof MemberJoinedEvent){
        System.out.println("MEMBERJOIN");
      }

    }
  }
}
