package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/")
public class QuestionController {
    @Autowired
   private QuestionService questionService;

    @RequestMapping(method=RequestMethod.POST, path="/question/create", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest,
                                                           @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        QuestionEntity questionEntity=new QuestionEntity();

      //  byte[] decode=Base64.getDecoder().decode(authorization.split("Basic ")[1]);
     //   String decodeText=new String(decode);
     //   String[] decodedArray=decodeText.split(":");

        UserAuthEntity userAuth= questionService.authenticate(authorization);

        questionEntity.setUuid(userAuth.getUuid());
        questionEntity.setContent(questionRequest.getContent());
        ZonedDateTime now=ZonedDateTime.now();
        questionEntity.setDate(now);
        questionEntity.setUser(userAuth.getUser());

        QuestionEntity userQuestion= questionService.saveUserQuestion(questionEntity);

        QuestionResponse questionResponse=new QuestionResponse().id(userQuestion.getUuid()).status("Question Saved");

        return new ResponseEntity<QuestionResponse>(questionResponse,HttpStatus.CREATED);
    }
}
