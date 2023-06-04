package de.fhdw.project.library.media.controller;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.service.request.MediaModelRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/media")
@CrossOrigin
public class MediaModelGeneralController {

    @Autowired
    private MediaModelRequestService mediaModelRequestService;

    @GetMapping("/{uuid}")
    public final ResponseEntity<String> onGet(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.mediaModelRequestService.getMedia(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/search")
    public final ResponseEntity<String> onSearch(@RequestHeader final String auth, @RequestParam(required = false) final String filter, @RequestParam(defaultValue = "1") final int page, @RequestParam(defaultValue = "10") final int size){
        try{
            return this.mediaModelRequestService.searchMedia(auth, filter, page, size);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @PostMapping("")
    public final ResponseEntity<String> onCreate(@RequestHeader final String auth, @RequestBody final String body){
        try{
            return this.mediaModelRequestService.createMedia(auth, body);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @PutMapping("/{uuid}")
    public final ResponseEntity<String> onEdit(@RequestHeader final String auth, @PathVariable final UUID uuid, @RequestBody final String body){
        try{
            return this.mediaModelRequestService.editMedia(auth, uuid, body);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @DeleteMapping("/{uuid}")
    public final ResponseEntity<String> onEdit(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.mediaModelRequestService.deleteMedia(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }


}
