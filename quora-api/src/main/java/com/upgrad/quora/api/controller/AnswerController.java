package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {
    @Autowired
    private AnswerService answerService;


    @RequestMapping (method=RequestMethod.POST, path="/question/{questionId}answer/create", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswers(final AnswerRequest answerRequest,@PathVariable("questionId") final String questionId,
                                                         @RequestHeader("authorization") final String authorization) throws InvalidQuestionException, AuthorizationFailedException {
        AnswerEntity answerEntity=new AnswerEntity();
        QuestionEntity questionEntity= answerService.authenticateQuestion(questionId);
        UserAuthEntity userAuthEntity= answerService.anthenticate(authorization);

        answerEntity.setAns(answerRequest.getAnswer());
        ZonedDateTime now=ZonedDateTime.now();
        answerEntity.setDate(now);
        answerEntity.setQuestion(questionEntity);
        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setUuid(UUID.randomUUID().toString());

        answerService.createAnswer(answerEntity);
        AnswerResponse answerResponse=new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);

    }
//editAnswerContent - "/answer/edit/{answerId}"

}
