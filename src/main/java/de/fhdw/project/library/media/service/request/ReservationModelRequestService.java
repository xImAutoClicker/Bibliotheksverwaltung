package de.fhdw.project.library.media.service.request;

import de.fhdw.project.library.media.service.ReservationModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationModelRequestService {
    @Autowired
    private ReservationModelService reservationModelService;
}
