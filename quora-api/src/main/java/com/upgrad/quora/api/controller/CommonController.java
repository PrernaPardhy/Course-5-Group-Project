package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")

public class CommonController {

    @Autowired
    private UserBusinessService userBusinessService;

   @RequestMapping(method=RequestMethod.POST, path="/userprofile/{userId}",consumes=MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId") final String userUuid,
                                                           @RequestHeader("authorization") final String authorization) throws AuthenticationFailedException, UserNotFoundException {
      UserEntity getAuthUser= userBusinessService.getUser(userUuid,authorization);
      UserDetailsResponse userDetailsResponse=new UserDetailsResponse().userName(getAuthUser.getUsername())
              .firstName(getAuthUser.getFirstName()).lastName(getAuthUser.getLastName()).contactNumber(getAuthUser.getContactNumber())
              .dob(getAuthUser.getDob()).country(getAuthUser.getCountry()).emailAddress(getAuthUser.getEmail())
              .aboutMe(getAuthUser.getAboutme());

          return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.FOUND);








    }
}
