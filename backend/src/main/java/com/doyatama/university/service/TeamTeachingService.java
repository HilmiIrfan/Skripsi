package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.*;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.TeamTeachingRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.*;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.List;

/**
 * @author alfa
 */

public class TeamTeachingService {
    private TeamTeachingRepository teamTeachingRepository = new TeamTeachingRepository();
    private LectureRepository lectureRepository = new LectureRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(TeamTeachingService.class);

    public PagedResponse<TeamTeaching> getAllTeamTeaching(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve TeamTeaching
        List<TeamTeaching> teamTeachingResponse = teamTeachingRepository.findAll(size);
        return new PagedResponse<>(teamTeachingResponse, teamTeachingResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public TeamTeaching createTeamTeaching(TeamTeachingRequest teamTeachingRequest) throws IOException {

        TeamTeaching teamTeaching = new TeamTeaching();

        Lecture lecture = lectureRepository.findById(teamTeachingRequest.getLecture());
        Lecture lecture2 = lectureRepository.findById(teamTeachingRequest.getLecture2());
        Lecture lecture3 = lectureRepository.findById(teamTeachingRequest.getLecture3());

        if (lecture.getName() != null && lecture2.getName() != null && lecture3.getName() != null) {
            teamTeaching.setName(teamTeachingRequest.getName());
            teamTeaching.setDescription(teamTeachingRequest.getDescription());
            teamTeaching.setLecture(lecture);
            teamTeaching.setLecture2(lecture2);
            teamTeaching.setLecture3(lecture3);

            return teamTeachingRepository.save(teamTeaching);
        } else {
            return null;
        }

    }

    public DefaultResponse<TeamTeaching> getTeamTeachingById(String teamTeachingId) throws IOException {
        // Retrieve TeamTeaching
        TeamTeaching teamTeachingResponse = teamTeachingRepository.findById(teamTeachingId);
        return new DefaultResponse<>(teamTeachingResponse.isValid() ? teamTeachingResponse : null,
                teamTeachingResponse.isValid() ? 1 : 0, "Successfully get data");

    }

    public TeamTeaching updateTeamTeaching(String teamTeachingId, TeamTeachingRequest teamTeachingRequest)
            throws IOException {
        // Create a new TeamTeaching object
        TeamTeaching teamTeaching = new TeamTeaching();

        Lecture lecture = lectureRepository.findById(teamTeachingRequest.getLecture());
        Lecture lecture2 = lectureRepository.findById(teamTeachingRequest.getLecture2());
        Lecture lecture3 = lectureRepository.findById(teamTeachingRequest.getLecture3());

        if (lecture.getName() != null && lecture2.getName() != null && lecture3.getName() != null) {
            // Set the properties on the TeamTeaching object
            teamTeaching.setName(teamTeachingRequest.getName());
            teamTeaching.setDescription(teamTeachingRequest.getDescription());
            teamTeaching.setLecture(lecture);
            teamTeaching.setLecture2(lecture2);
            teamTeaching.setLecture3(lecture3);

            return teamTeachingRepository.update(teamTeachingId, teamTeaching);
        } else {
            return null;
        }
    }

    public void deleteTeamTeachingById(String teamTeachingId) throws IOException {
        TeamTeaching teamTeachingResponse = teamTeachingRepository.findById(teamTeachingId);
        if (teamTeachingResponse.isValid()) {
            teamTeachingRepository.deleteById(teamTeachingId);
        } else {
            throw new ResourceNotFoundException("TeamTeaching", "id", teamTeachingId);
        }
    }

}
