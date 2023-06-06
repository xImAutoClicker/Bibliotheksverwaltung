package de.fhdw.project.library.media.controller;

import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.media.service.request.MediaModelRequestService;
import de.fhdw.project.library.media.service.request.ReservationModelRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/reservation")
@CrossOrigin
public class ReservationModelGeneralController {

    @Autowired
    private ReservationModelRequestService reservationModelRequestService;

    /**
     * Add - MediaId, UserId
     * Update Status - ReservationId - StatusCode (nach enum liste 0 = OPEN | 1 = ACCEPTED)
     * GetReservation
     * Delete MediaId, UserId
     */


    @PostMapping("")
    public final ResponseEntity<String> onCreate(@RequestHeader final String auth, @RequestBody final String body){
        try{
            return this.reservationModelRequestService.createReservation(auth, body);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @PutMapping("/{uuid}")
    public final ResponseEntity<String> onUpdate(@RequestHeader final String auth, @PathVariable final UUID uuid, @RequestBody final String body){
        try{
            return this.reservationModelRequestService.updateReservation(auth, uuid, body);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/{uuid}")
    public final ResponseEntity<String> onGet(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.reservationModelRequestService.getReservation(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @DeleteMapping("/{uuid}")
    public final ResponseEntity<String> onDelete(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.reservationModelRequestService.deleteReservation(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/user/{uuid}")
    public final ResponseEntity<String> onGetReservationOfUser(@RequestHeader final String auth, @PathVariable final UUID uuid){
        try{
            return this.reservationModelRequestService.getReservationsOfUser(auth, uuid);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("")
    public final ResponseEntity<String> onGet(@RequestHeader final String auth, @RequestParam(defaultValue = "1") final int page, @RequestParam(defaultValue = "10") final int size){
        try{
            return this.reservationModelRequestService.getReservations(auth, page, size);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }

    @GetMapping("/search")
    public final ResponseEntity<String> onSearch(@RequestHeader final String auth, @RequestParam(defaultValue = "1") final int page, @RequestParam(defaultValue = "10") final int size){
        try{
            return this.reservationModelRequestService.searchReservation(auth, page, size);
        }catch (LibraryException e){
            return e.toResponseEntity();
        }
    }
}
