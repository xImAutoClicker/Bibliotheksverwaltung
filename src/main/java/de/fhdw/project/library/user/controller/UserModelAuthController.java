package de.fhdw.project.library.user.controller;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.user.service.request.UserModelRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user/auth/")
@CrossOrigin
public class UserModelAuthController {

    @Autowired
    private UserModelRequestService userModelRequestService;

    @PostMapping("register")
    public final ResponseEntity<String> onRegister(@RequestBody final String body){
        try{
            return this.userModelRequestService.registerUser(body);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @PostMapping("login")
    public final ResponseEntity<String> onLogin(@RequestBody final String body){
        try{
            return this.userModelRequestService.loginUser(body);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

}
