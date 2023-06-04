package de.fhdw.project.library.media.model.borrow;

import de.fhdw.project.library.media.model.borrow.BorrowResponseModel;
import de.fhdw.project.library.util.response.AbstractResponse;
import lombok.Builder;

import java.util.List;

@Builder
public class BorrowSearchResponseModel extends AbstractResponse {
    private long count;
    private List<BorrowResponseModel> borrows;
}
