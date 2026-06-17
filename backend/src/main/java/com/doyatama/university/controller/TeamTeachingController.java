package com.doyatama.university.controller;

import com.doyatama.university.model.TeamTeaching;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.TeamTeachingRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.TeamTeachingRepository;
import com.doyatama.university.service.TeamTeachingService;
import com.doyatama.university.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;


/**
 * @author alfa
 */
@RestController
@RequestMapping("/api/team-teaching")
public class TeamTeachingController {
    private TeamTeachingService teamTeachingService = new TeamTeachingService();

    TeamTeachingRepository teamTeachingRepository;

    @GetMapping
    public PagedResponse<TeamTeaching> getTeamTeachings(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return teamTeachingService.getAllTeamTeaching(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createTeamTeaching(@Valid @RequestBody TeamTeachingRequest teamTeachingRequest) throws IOException {
        TeamTeaching teamTeaching = teamTeachingService.createTeamTeaching(teamTeachingRequest);

        if(teamTeaching == null){
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Lecture ID not found"));
        }else{
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{teamTeachingId}")
                    .buildAndExpand(teamTeaching.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "TeamTeaching Created Successfully"));
        }
    }

    @GetMapping("/{teamTeachingId}")
    public DefaultResponse<TeamTeaching> getTeamTeachingById(@PathVariable String teamTeachingId) throws IOException {
        return teamTeachingService.getTeamTeachingById(teamTeachingId);
    }

    @PutMapping("/{teamTeachingId}")
    public ResponseEntity<?> updateTeamTeaching(@PathVariable String teamTeachingId,
                                                @Valid @RequestBody TeamTeachingRequest teamTeachingRequest) throws IOException {
        TeamTeaching teamTeaching = teamTeachingService.updateTeamTeaching(teamTeachingId, teamTeachingRequest);

        if(teamTeaching == null){
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Lecture ID not found"));
        }else{
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{teamTeachingId}")
                    .buildAndExpand(teamTeaching.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "TeamTeaching Updated Successfully"));
        }
    }

    @DeleteMapping("/{teamTeachingId}")
    public HttpStatus deleteTeamTeaching(@PathVariable (value = "teamTeachingId") String teamTeachingId) throws IOException {
        teamTeachingService.deleteTeamTeachingById(teamTeachingId);
        return HttpStatus.FORBIDDEN;
    }

}
