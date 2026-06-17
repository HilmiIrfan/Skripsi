package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Chapter;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.ChapterRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.ChapterRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Service
public class ChapterService {
    private ChapterRepository chapterRepository = new ChapterRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(ChapterService.class);

    public PagedResponse<Chapter> getAllChapter(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<Chapter> chapterResponse = chapterRepository.findAll(size);

        return new PagedResponse<>(chapterResponse, chapterResponse.size(), "Successfully get data", 200);
    }

    public Chapter createChapter(ChapterRequest chapterRequest) throws IOException {
        Chapter chapter = new Chapter();

        chapter.setName(chapterRequest.getName());
        chapter.setDescription(chapterRequest.getDescription());
        chapter.setCourse_id(chapterRequest.getCourse_id());

        return chapterRepository.save(chapter);
    }

    public DefaultResponse<Chapter> getChapterById(String chapterId) throws IOException {
        // Retrieve Chapter
        Chapter chapterResponse = chapterRepository.findById(chapterId);
        return new DefaultResponse<>(chapterResponse.isValid() ? chapterResponse : null,
                chapterResponse.isValid() ? 1 : 0, "Successfully get data");
    }

    public Chapter updateChapter(String chapterId, ChapterRequest chapterRequest) throws IOException {
        Chapter chapter = new Chapter();
        chapter.setName(chapterRequest.getName());
        chapter.setDescription(chapterRequest.getDescription());
        chapter.setCourse_id(chapterRequest.getCourse_id());

        return chapterRepository.update(chapterId, chapter);
    }

    public void deleteChapterById(String chapterId) throws IOException {
        Chapter chapterResponse = chapterRepository.findById(chapterId);
        if (chapterResponse.isValid()) {
            chapterRepository.deleteById(chapterId);
        } else {
            throw new ResourceNotFoundException("Chapter", "id", chapterId);
        }
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

}
