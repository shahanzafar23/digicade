package com.digicade.web.rest;

import com.digicade.repository.GameLevelRepository;
import com.digicade.service.GameLevelService;
import com.digicade.service.dto.GameLevelDTO;
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
 * REST controller for managing {@link com.digicade.domain.GameLevel}.
 */
@RestController
@RequestMapping("/api")
public class GameLevelResource {

    private final Logger log = LoggerFactory.getLogger(GameLevelResource.class);

    private static final String ENTITY_NAME = "gameLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameLevelService gameLevelService;

    private final GameLevelRepository gameLevelRepository;

    public GameLevelResource(GameLevelService gameLevelService, GameLevelRepository gameLevelRepository) {
        this.gameLevelService = gameLevelService;
        this.gameLevelRepository = gameLevelRepository;
    }

    /**
     * {@code POST  /game-levels} : Create a new gameLevel.
     *
     * @param gameLevelDTO the gameLevelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameLevelDTO, or with status {@code 400 (Bad Request)} if the gameLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-levels")
    public ResponseEntity<GameLevelDTO> createGameLevel(@RequestBody GameLevelDTO gameLevelDTO) throws URISyntaxException {
        log.debug("REST request to save GameLevel : {}", gameLevelDTO);
        if (gameLevelDTO.getId() != null) {
            throw new BadRequestAlertException("A new gameLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameLevelDTO result = gameLevelService.save(gameLevelDTO);
        return ResponseEntity
            .created(new URI("/api/game-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-levels/:id} : Updates an existing gameLevel.
     *
     * @param id the id of the gameLevelDTO to save.
     * @param gameLevelDTO the gameLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameLevelDTO,
     * or with status {@code 400 (Bad Request)} if the gameLevelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-levels/{id}")
    public ResponseEntity<GameLevelDTO> updateGameLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameLevelDTO gameLevelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GameLevel : {}, {}", id, gameLevelDTO);
        if (gameLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameLevelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameLevelDTO result = gameLevelService.update(gameLevelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameLevelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-levels/:id} : Partial updates given fields of an existing gameLevel, field will ignore if it is null
     *
     * @param id the id of the gameLevelDTO to save.
     * @param gameLevelDTO the gameLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameLevelDTO,
     * or with status {@code 400 (Bad Request)} if the gameLevelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gameLevelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-levels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameLevelDTO> partialUpdateGameLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameLevelDTO gameLevelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameLevel partially : {}, {}", id, gameLevelDTO);
        if (gameLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameLevelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameLevelDTO> result = gameLevelService.partialUpdate(gameLevelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameLevelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /game-levels} : get all the gameLevels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameLevels in body.
     */
    @GetMapping("/game-levels")
    public ResponseEntity<List<GameLevelDTO>> getAllGameLevels(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of GameLevels");
        Page<GameLevelDTO> page = gameLevelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /game-levels/:id} : get the "id" gameLevel.
     *
     * @param id the id of the gameLevelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameLevelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-levels/{id}")
    public ResponseEntity<GameLevelDTO> getGameLevel(@PathVariable Long id) {
        log.debug("REST request to get GameLevel : {}", id);
        Optional<GameLevelDTO> gameLevelDTO = gameLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameLevelDTO);
    }

    /**
     * {@code DELETE  /game-levels/:id} : delete the "id" gameLevel.
     *
     * @param id the id of the gameLevelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-levels/{id}")
    public ResponseEntity<Void> deleteGameLevel(@PathVariable Long id) {
        log.debug("REST request to delete GameLevel : {}", id);
        gameLevelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
