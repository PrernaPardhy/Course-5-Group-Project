package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
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
public class AnswerController {
    @Autowired
    private AnswerService answerService;


    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswers(final AnswerRequest answerRequest, @PathVariable("questionId") final String questionId,
                                                        @RequestHeader("authorization") final String authorization) throws InvalidQuestionException, AuthorizationFailedException {
        AnswerEntity answerEntity = new AnswerEntity();
        QuestionEntity questionEntity = answerService.authenticateQuestion(questionId);
        UserAuthEntity userAuthEntity = answerService.authenticate(authorization);

        answerEntity.setAns(answerRequest.getAnswer());
        ZonedDateTime now = ZonedDateTime.now();
        answerEntity.setDate(now);
        answerEntity.setQuestion(questionEntity);
        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setUuid(UUID.randomUUID().toString());

        answerService.createAnswer(answerEntity);
        AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(final AnswerEditRequest answerEditRequest,
                                                                @PathVariable("answerId") final String answerId,
                                                                @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthEntity userAuth = answerService.authenticateEdit(authorization);

        AnswerEntity answerEntity = answerService.authenticateUser(userAuth, answerId);
        answerEntity.setAns(answerEditRequest.getContent());


        answerService.editAnswer(answerEntity);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(answerEntity.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerId,
                                                               @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = answerService.authenticateDelete(authorization);
        AnswerEntity answerEntity = answerService.authenticateUserDelete(userAuthEntity, answerId);

        answerService.deleteAnswer(answerEntity);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.GET, path="/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDetailsResponse> getAllAnswersToQuestion (@PathVariable("questionId") final String questionId,
                                                                          @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        answerService.authenticate(authorization);


        List<AnswerEntity> answerEntity= answerService.getAnswers(questionId);
        AnswerDetailsResponse answerDetailsResponse=new AnswerDetailsResponse();
        String uuid="";
        String answers="";
        String questions="";
        for(AnswerEntity ans: answerEntity){
            uuid=ans.getUuid()+"   "+uuid;
            answers=ans.getAns()+"   "+answers;
            questions=ans.getQuestion().getContent();
            answerDetailsResponse.questionContent(questions);
        }
        answerDetailsResponse.id(uuid).answerContent(answers);

        return new ResponseEntity<AnswerDetailsResponse>(answerDetailsResponse,HttpStatus.OK);
    }
}



