package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDetailsResponse;
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
import java.util.List;

@RestController
@RequestMapping("/")
public class QuestionController {
    @Autowired
   private QuestionService questionService;

    @RequestMapping(method=RequestMethod.POST, path="/question/create", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest,
                                                           @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        QuestionEntity questionEntity=new QuestionEntity();
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

    @RequestMapping(method=RequestMethod.GET, path="/question/all", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
      UserAuthEntity userAuth= questionService.authenticate(authorization);
      String userAuth1=userAuth.getUuid();
      String usrAuth2=userAuth1;

     List<QuestionEntity> listQuestions=questionService.getQuestions(userAuth.getUuid());

     QuestionDetailsResponse questionDetailsResponse=new QuestionDetailsResponse().id(userAuth.getUuid());
        String questionString="";
        int i=listQuestions.size();
     for(QuestionEntity Qe:listQuestions){

         questionString=i+") "+Qe.getContent()+" "+questionString;
         i--;


     }
        questionDetailsResponse.content(questionString);


    return new ResponseEntity<QuestionDetailsResponse>(questionDetailsResponse,HttpStatus.OK);




    }
}
