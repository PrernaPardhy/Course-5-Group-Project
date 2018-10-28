package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")


public class AdminController {
    @Autowired
    private AdminBusinessService adminBusinessService;

    // ENDPOINT - userDelete - "/admin/user/{userId}"
    //
    //This endpoint is used to delete a user from the Quora Application. Only an admin is authorized to access this endpoint.
    @RequestMapping(method=RequestMethod.DELETE, path="/admin/user/{userId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable("userId") final String userId,
                                                         @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        String userUuid=adminBusinessService.getUserAuth(userId,authorization);

        UserDeleteResponse userDeleteResponse=new UserDeleteResponse().id(userUuid).status("User Deleted");

        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse,HttpStatus.OK);

    }
}
