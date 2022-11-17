package com.digicade.web.rest;

import com.digicade.repository.HighScoreRepository;
import com.digicade.service.HighScoreService;
import com.digicade.service.dto.HighScoreDTO;
import com.digicade.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.digicade.domain.HighScore}.
 */
@RestController
@RequestMapping("/api")
public class HighScoreResource {

    private final Logger log = LoggerFactory.getLogger(HighScoreResource.class);

    private static final String ENTITY_NAME = "highScore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HighScoreService highScoreService;

    private final HighScoreRepository highScoreRepository;

    public HighScoreResource(HighScoreService highScoreService, HighScoreRepository highScoreRepository) {
        this.highScoreService = highScoreService;
        this.highScoreRepository = highScoreRepository;
    }

    /**
     * {@code POST  /high-scores} : Create a new highScore.
     *
     * @param highScoreDTO the highScoreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new highScoreDTO, or with status {@code 400 (Bad Request)} if the highScore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/high-scores")
    public ResponseEntity<HighScoreDTO> createHighScore(@RequestBody HighScoreDTO highScoreDTO) throws URISyntaxException {
        log.debug("REST request to save HighScore : {}", highScoreDTO);
        if (highScoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new highScore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HighScoreDTO result = highScoreService.save(highScoreDTO);
        return ResponseEntity
            .created(new URI("/api/high-scores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /high-scores/:id} : Updates an existing highScore.
     *
     * @param id the id of the highScoreDTO to save.
     * @param highScoreDTO the highScoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated highScoreDTO,
     * or with status {@code 400 (Bad Request)} if the highScoreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the highScoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/high-scores/{id}")
    public ResponseEntity<HighScoreDTO> updateHighScore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HighScoreDTO highScoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HighScore : {}, {}", id, highScoreDTO);
        if (highScoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, highScoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!highScoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HighScoreDTO result = highScoreService.update(highScoreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, highScoreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /high-scores/:id} : Partial updates given fields of an existing highScore, field will ignore if it is null
     *
     * @param id the id of the highScoreDTO to save.
     * @param highScoreDTO the highScoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated highScoreDTO,
     * or with status {@code 400 (Bad Request)} if the highScoreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the highScoreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the highScoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/high-scores/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HighScoreDTO> partialUpdateHighScore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HighScoreDTO highScoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HighScore partially : {}, {}", id, highScoreDTO);
        if (highScoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, highScoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!highScoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HighScoreDTO> result = highScoreService.partialUpdate(highScoreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, highScoreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /high-scores} : get all the highScores.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of highScores in body.
     */
    @GetMapping("/high-scores")
    public ResponseEntity<List<HighScoreDTO>> getAllHighScores(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of HighScores");
        Page<HighScoreDTO> page = highScoreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /high-scores/:id} : get the "id" highScore.
     *
     * @param id the id of the highScoreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the highScoreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/high-scores/{id}")
    public ResponseEntity<HighScoreDTO> getHighScore(@PathVariable Long id) {
        log.debug("REST request to get HighScore : {}", id);
        Optional<HighScoreDTO> highScoreDTO = highScoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(highScoreDTO);
    }

    /**
     * {@code DELETE  /high-scores/:id} : delete the "id" highScore.
     *
     * @param id the id of the highScoreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/high-scores/{id}")
    public ResponseEntity<Void> deleteHighScore(@PathVariable Long id) {
        log.debug("REST request to delete HighScore : {}", id);
        highScoreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
