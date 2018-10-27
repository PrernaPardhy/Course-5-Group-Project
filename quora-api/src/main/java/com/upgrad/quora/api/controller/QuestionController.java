package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

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
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        ZonedDateTime now=ZonedDateTime.now();
        questionEntity.setDate(now);
        questionEntity.setUser(userAuth.getUser());

        QuestionEntity userQuestion= questionService.saveUserQuestion(questionEntity);

        QuestionResponse questionResponse=new QuestionResponse().id(userQuestion.getUuid()).status("Question Created");

        return new ResponseEntity<QuestionResponse>(questionResponse,HttpStatus.CREATED);
    }

    @RequestMapping(method=RequestMethod.GET, path="/question/all", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        UserAuthEntity userAuth= questionService.authenticate(authorization);

        List<QuestionEntity> listQuestions=questionService.getQuestions();

        String questionString="";
        String questionId="";
        int i=listQuestions.size();
        for(QuestionEntity Qe:listQuestions){

            questionId=i+")  "+Qe.getUuid()+"  "+questionId;
            questionString=i+")  "+Qe.getContent()+" "+questionString;
            i--;


        }
        QuestionDetailsResponse questionDetailsResponse=new  QuestionDetailsResponse().id(questionId).content(questionString);


        return new ResponseEntity<QuestionDetailsResponse>(questionDetailsResponse,HttpStatus.OK);

    }

    @RequestMapping(method=RequestMethod.PUT,path="/question/edit/{questionId}",consumes=MediaType.APPLICATION_JSON_UTF8_VALUE,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent (final QuestionEditRequest questionEditRequest,
                                                                     @PathVariable("questionId") final String questionId,
                                                                     @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuth= questionService.authenticateEdit(authorization);
        QuestionEntity questionEntity= questionService.getQuestionAuthenticateEdit(userAuth,questionId);
        questionEntity.setContent(questionEditRequest.getContent());
        ZonedDateTime now=ZonedDateTime.now();
        questionEntity.setDate(now);
        questionService.updateUserQuestion(questionEntity);

        QuestionEditResponse questionEditResponse=new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.OK);

    }

    @RequestMapping(method=RequestMethod.DELETE,path="/question/delete/{questionId}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionId,
                                                                 @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuth = questionService.authenticateDelete(authorization);
        QuestionEntity questionEntity = questionService.getQuestionAuthenticate(userAuth, questionId);
        questionService.deleteQuestion(questionEntity);

        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid()).status("QUESTION DELETED");

        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.GET,path="/all1/{userId}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestionsByUser(@PathVariable("userId") final String userUuid,
                                                                         @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuth= questionService.authenticate(authorization);

        List<QuestionEntity> listQuestions=questionService.getUserQuestion(userUuid);


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

