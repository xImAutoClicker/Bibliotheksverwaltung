package de.fhdw.project.library.media.service.request;

import de.fhdw.project.library.media.service.BorrowModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowModelRequestService {

    @Autowired
    private BorrowModelService borrowModelService;
}
