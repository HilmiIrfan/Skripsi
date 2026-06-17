
package com.doyatama.university.controller;

import com.doyatama.university.model.Season;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.SeasonRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.SeasonService;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author senja
 */

@RestController
@RequestMapping("/api/season")
public class SeasonController {
    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @GetMapping
    public PagedResponse<Season> getSeason(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return seasonService.getAllSeason(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createSeason(@Valid @RequestBody SeasonRequest seasonRequest) throws IOException {
        Season season = seasonService.createSeason(seasonRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{seasonId}")
                .buildAndExpand(season.getIdSeason()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Season Created Successfully"));
    }

    @GetMapping("/{seasonId}")
    public DefaultResponse<Season> getSeasonById(@PathVariable String seasonId) throws IOException {
        return seasonService.getSeasonById(seasonId);
    }

    // @PutMapping("/{seasonId}")
    // public ResponseEntity<?> updateSeason(@PathVariable String seasonId,
    // @Valid @RequestBody SeasonRequest seasonRequest) throws IOException {
    // Season season = seasonService.updateSeason(seasonId, seasonRequest);
    //
    // URI location = ServletUriComponentsBuilder
    // .fromCurrentRequest().path("/{seasonId}")
    // .buildAndExpand(season.getIdSeason()).toUri();
    //
    // return ResponseEntity.created(location)
    // .body(new ApiResponse(true, "Season Updated Successfully"));
    // }

    @DeleteMapping("/{seasonId}")
    public HttpStatus deleteSeason(@PathVariable String seasonId) throws IOException {
        seasonService.deleteSeasonById(seasonId);
        return HttpStatus.NO_CONTENT;
    }
}
