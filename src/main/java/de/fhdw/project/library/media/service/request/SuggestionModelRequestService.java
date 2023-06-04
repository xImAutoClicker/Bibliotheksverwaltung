package de.fhdw.project.library.media.service.request;

import de.fhdw.project.library.media.service.SuggestionModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestionModelRequestService {
    @Autowired
    private SuggestionModelService suggestionModelService;
}
