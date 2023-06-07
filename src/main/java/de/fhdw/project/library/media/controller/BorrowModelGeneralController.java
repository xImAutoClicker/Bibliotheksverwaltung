package de.fhdw.project.library.media.controller;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.model.borrow.BorrowModel;
import de.fhdw.project.library.media.service.request.BorrowModelRequestService;
import de.fhdw.project.library.media.service.request.SuggestionModelRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/borrow")
@CrossOrigin
public class BorrowModelGeneralController {

    @Autowired
    private BorrowModelRequestService borrowModelRequestService;

    @PostMapping("")
    public final ResponseEntity<String> onCreate(@RequestHeader final String auth, @RequestBody final String body){
        try{
            return this.borrowModelRequestService.createBorrow(auth, body);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @PutMapping("/{uuid}")
    public final ResponseEntity<String> onReturn(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.borrowModelRequestService.returnMedia(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/{uuid}")
    public final ResponseEntity<String> onGet(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.borrowModelRequestService.getBorrow(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/user/{uuid}")
    public final ResponseEntity<String> onGetBorrowsOfUser(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.borrowModelRequestService.getBorrowOfUser(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/user/punishes/{uuid}")
    public final ResponseEntity<String> onGetOverdayBorrowsOfUser(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.borrowModelRequestService.getBorrowOverdaysOfUser(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/search")
    public final ResponseEntity<String> onGet(@RequestHeader final String auth, @RequestParam(defaultValue = "1") final int page, @RequestParam(defaultValue = "10") final int size, @RequestParam(defaultValue = "-1") final int statusType){
        try{
            return this.borrowModelRequestService.getBorrows(auth, page, size, statusType == -1 ? null : BorrowModel.BorrowStatusType.values()[statusType]);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

}
