package de.fhdw.project.library.media.controller;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.service.request.MediaHeadModelRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/media_head")
@CrossOrigin
public class MediaHeadModelGeneralController {

    @Autowired
    private MediaHeadModelRequestService mediaHeadModelRequestService;

    @GetMapping("/{isbn}")
    public final ResponseEntity<String> onGet(@RequestHeader final String auth, @PathVariable final String isbn){
        try{
            return this.mediaHeadModelRequestService.getMediaHead(auth, isbn);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }
    @GetMapping("/search")
    public final ResponseEntity<String> onSearch(@RequestHeader final String auth, @RequestParam(required = false) final String filter, @RequestParam(defaultValue = "1") final int page, @RequestParam(defaultValue = "10") final int size){
        try{
            return this.mediaHeadModelRequestService.searchMediaHead(auth, filter, page, size);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @PostMapping("")
    public final ResponseEntity<String> onCreate(@RequestHeader final String auth, @RequestBody final String body){
        try{
            return this.mediaHeadModelRequestService.createMediaHead(auth, body);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @PutMapping("/{isbn}")
    public final ResponseEntity<String> onEdit(@RequestHeader final String auth, @PathVariable final String isbn, @RequestBody final String body){
        try{
            return this.mediaHeadModelRequestService.editMediaHead(auth, isbn, body);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @DeleteMapping("/{isbn}")
    public final ResponseEntity<String> onEdit(@RequestHeader final String auth, @PathVariable final String isbn){
        try{
            return this.mediaHeadModelRequestService.deleteMediaHead(auth, isbn);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }
}
