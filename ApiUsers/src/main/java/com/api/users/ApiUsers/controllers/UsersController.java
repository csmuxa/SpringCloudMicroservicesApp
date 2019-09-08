package com.api.users.ApiUsers.controllers;

import com.api.users.ApiUsers.model.CreateUserRequestModel;
import com.api.users.ApiUsers.model.CreateUserResponseModel;
import com.api.users.ApiUsers.model.UserResponseModel;
import com.api.users.ApiUsers.service.UserService;
import com.api.users.ApiUsers.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
public class UsersController {

    @Autowired
    private Environment env;
    @Autowired
    UserService userService;


    @GetMapping("/status/check")
    public String status() {
        return "working on  port" + env.getProperty("local.server.port");
    }


    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_XML_VALUE ,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel model) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(model, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        CreateUserResponseModel returnValue=modelMapper.map(createdUser,CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }


    @GetMapping(value = "/{userId}",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId")String userId){

        UserDto userDto=userService.getUserByUserId(userId);
        UserResponseModel returnValue =  new ModelMapper().map(userDto,UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
