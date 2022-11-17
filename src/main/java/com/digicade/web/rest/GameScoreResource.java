package com.digicade.web.rest;

import com.digicade.repository.GameScoreRepository;
import com.digicade.service.GameScoreService;
import com.digicade.service.dto.GameScoreDTO;
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
 * REST controller for managing {@link com.digicade.domain.GameScore}.
 */
@RestController
@RequestMapping("/api")
public class GameScoreResource {

    private final Logger log = LoggerFactory.getLogger(GameScoreResource.class);

    private static final String ENTITY_NAME = "gameScore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameScoreService gameScoreService;

    private final GameScoreRepository gameScoreRepository;

    public GameScoreResource(GameScoreService gameScoreService, GameScoreRepository gameScoreRepository) {
        this.gameScoreService = gameScoreService;
        this.gameScoreRepository = gameScoreRepository;
    }

    /**
     * {@code POST  /game-scores} : Create a new gameScore.
     *
     * @param gameScoreDTO the gameScoreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameScoreDTO, or with status {@code 400 (Bad Request)} if the gameScore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-scores")
    public ResponseEntity<GameScoreDTO> createGameScore(@RequestBody GameScoreDTO gameScoreDTO) throws URISyntaxException {
        log.debug("REST request to save GameScore : {}", gameScoreDTO);
        if (gameScoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new gameScore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameScoreDTO result = gameScoreService.save(gameScoreDTO);
        return ResponseEntity
            .created(new URI("/api/game-scores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-scores/:id} : Updates an existing gameScore.
     *
     * @param id the id of the gameScoreDTO to save.
     * @param gameScoreDTO the gameScoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameScoreDTO,
     * or with status {@code 400 (Bad Request)} if the gameScoreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameScoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-scores/{id}")
    public ResponseEntity<GameScoreDTO> updateGameScore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameScoreDTO gameScoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GameScore : {}, {}", id, gameScoreDTO);
        if (gameScoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameScoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameScoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameScoreDTO result = gameScoreService.update(gameScoreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameScoreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-scores/:id} : Partial updates given fields of an existing gameScore, field will ignore if it is null
     *
     * @param id the id of the gameScoreDTO to save.
     * @param gameScoreDTO the gameScoreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameScoreDTO,
     * or with status {@code 400 (Bad Request)} if the gameScoreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gameScoreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameScoreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-scores/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameScoreDTO> partialUpdateGameScore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameScoreDTO gameScoreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameScore partially : {}, {}", id, gameScoreDTO);
        if (gameScoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameScoreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameScoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameScoreDTO> result = gameScoreService.partialUpdate(gameScoreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameScoreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /game-scores} : get all the gameScores.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameScores in body.
     */
    @GetMapping("/game-scores")
    public ResponseEntity<List<GameScoreDTO>> getAllGameScores(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of GameScores");
        Page<GameScoreDTO> page = gameScoreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /game-scores/:id} : get the "id" gameScore.
     *
     * @param id the id of the gameScoreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameScoreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-scores/{id}")
    public ResponseEntity<GameScoreDTO> getGameScore(@PathVariable Long id) {
        log.debug("REST request to get GameScore : {}", id);
        Optional<GameScoreDTO> gameScoreDTO = gameScoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameScoreDTO);
    }

    /**
     * {@code DELETE  /game-scores/:id} : delete the "id" gameScore.
     *
     * @param id the id of the gameScoreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-scores/{id}")
    public ResponseEntity<Void> deleteGameScore(@PathVariable Long id) {
        log.debug("REST request to delete GameScore : {}", id);
        gameScoreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
