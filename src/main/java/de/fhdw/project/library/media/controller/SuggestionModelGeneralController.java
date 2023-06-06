package de.fhdw.project.library.media.controller;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.service.request.SuggestionModelRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/suggestion")
@CrossOrigin
public class SuggestionModelGeneralController {

    @Autowired
    private SuggestionModelRequestService suggestionModelRequestService;


    @PostMapping("")
    public final ResponseEntity<String> onCreate(@RequestHeader final String auth, @RequestBody final String body){
        try{
            return this.suggestionModelRequestService.createSuggestion(auth, body);
        } catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @PutMapping("/{uuid}")
    public final ResponseEntity<String> onUpdate(@RequestHeader final String auth, @PathVariable final UUID uuid, @RequestBody final String body){
        try{
            return this.suggestionModelRequestService.updateSuggestion(auth, uuid, body);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/{uuid}")
    public final ResponseEntity<String> onGet(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.suggestionModelRequestService.getSuggestion(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/user/{uuid}")
    public final ResponseEntity<String> onGetSuggestionOfUser(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.suggestionModelRequestService.getSuggestionUser(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("")
    public final ResponseEntity<String> onGet(@RequestHeader final String auth, @RequestParam(defaultValue = "1") final int page, @RequestParam(defaultValue = "10") final int size){
        try{
            return this.suggestionModelRequestService.getSuggestions(auth, page, size);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/search")
    public final ResponseEntity<String> onSearch(@RequestHeader final String auth, @RequestParam(required = false) final String filter, @RequestParam(defaultValue = "1") final int page, @RequestParam(defaultValue = "10") final int size){
        try{
            return this.suggestionModelRequestService.searchSuggestion(auth, filter, page, size);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @DeleteMapping("/{uuid}")
    public final ResponseEntity<String> onDelete(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.suggestionModelRequestService.deleteSuggestion(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }
}
