package SpringCloudProject.project.ui.controller;


import SpringCloudProject.project.ui.model.request.UserDetailsRequestModel;
import SpringCloudProject.project.ui.model.response.UserRest;
import SpringCloudProject.project.userservice.UserService;
import SpringCloudProject.project.userservice.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {
    Map<String,UserRest> users;

    @Autowired
    UserService userService;


    @GetMapping(path = "/{userId}",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserRest> getUser(@PathVariable String userId){

        if (users.containsKey(userId)){
            return new ResponseEntity<>(users.get(userId), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserDetailsRequestModel model){
        UserRest returnValue= userService.createUser(model);


        return new ResponseEntity<>(returnValue, HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = "/{userId}",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public UserRest updateUser(@PathVariable String userId,@Valid @RequestBody UserDetailsRequestModel model){
        UserRest storedUserDetails =users.get(userId);
        storedUserDetails.setFirstName(model.getFirstName());
        storedUserDetails.setLastName(model.getLastName());

        users.put(userId,storedUserDetails);

        return storedUserDetails;

}

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<UserRest> deleteUser(@PathVariable String userId){
        users.remove(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
