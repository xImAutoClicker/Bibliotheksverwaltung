package de.fhdw.project.library.user.controller;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.user.service.request.UserModelRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/user/profile")
@CrossOrigin
public class UserModelProfileController {

    @Autowired
    private UserModelRequestService userModelRequestService;

    @GetMapping("/{uuid}")
    public final ResponseEntity<String> onGet(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.userModelRequestService.getUser(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/search")
    public final ResponseEntity<String> onGetList(@RequestHeader final String auth, @RequestParam(required = false) final String filter, @RequestParam(defaultValue = "1") final int page, @RequestParam(defaultValue = "10") final int size){
        try{
            return this.userModelRequestService.getUserList(auth, filter, page, size);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

}
