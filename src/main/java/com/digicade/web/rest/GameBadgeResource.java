package com.digicade.web.rest;

import com.digicade.repository.GameBadgeRepository;
import com.digicade.service.GameBadgeService;
import com.digicade.service.dto.GameBadgeDTO;
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
 * REST controller for managing {@link com.digicade.domain.GameBadge}.
 */
@RestController
@RequestMapping("/api")
public class GameBadgeResource {

    private final Logger log = LoggerFactory.getLogger(GameBadgeResource.class);

    private static final String ENTITY_NAME = "gameBadge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameBadgeService gameBadgeService;

    private final GameBadgeRepository gameBadgeRepository;

    public GameBadgeResource(GameBadgeService gameBadgeService, GameBadgeRepository gameBadgeRepository) {
        this.gameBadgeService = gameBadgeService;
        this.gameBadgeRepository = gameBadgeRepository;
    }

    /**
     * {@code POST  /game-badges} : Create a new gameBadge.
     *
     * @param gameBadgeDTO the gameBadgeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameBadgeDTO, or with status {@code 400 (Bad Request)} if the gameBadge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-badges")
    public ResponseEntity<GameBadgeDTO> createGameBadge(@RequestBody GameBadgeDTO gameBadgeDTO) throws URISyntaxException {
        log.debug("REST request to save GameBadge : {}", gameBadgeDTO);
        if (gameBadgeDTO.getId() != null) {
            throw new BadRequestAlertException("A new gameBadge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameBadgeDTO result = gameBadgeService.save(gameBadgeDTO);
        return ResponseEntity
            .created(new URI("/api/game-badges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-badges/:id} : Updates an existing gameBadge.
     *
     * @param id the id of the gameBadgeDTO to save.
     * @param gameBadgeDTO the gameBadgeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameBadgeDTO,
     * or with status {@code 400 (Bad Request)} if the gameBadgeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameBadgeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-badges/{id}")
    public ResponseEntity<GameBadgeDTO> updateGameBadge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameBadgeDTO gameBadgeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GameBadge : {}, {}", id, gameBadgeDTO);
        if (gameBadgeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameBadgeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameBadgeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameBadgeDTO result = gameBadgeService.update(gameBadgeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameBadgeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-badges/:id} : Partial updates given fields of an existing gameBadge, field will ignore if it is null
     *
     * @param id the id of the gameBadgeDTO to save.
     * @param gameBadgeDTO the gameBadgeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameBadgeDTO,
     * or with status {@code 400 (Bad Request)} if the gameBadgeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gameBadgeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameBadgeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-badges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameBadgeDTO> partialUpdateGameBadge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameBadgeDTO gameBadgeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameBadge partially : {}, {}", id, gameBadgeDTO);
        if (gameBadgeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameBadgeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameBadgeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameBadgeDTO> result = gameBadgeService.partialUpdate(gameBadgeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameBadgeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /game-badges} : get all the gameBadges.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameBadges in body.
     */
    @GetMapping("/game-badges")
    public ResponseEntity<List<GameBadgeDTO>> getAllGameBadges(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of GameBadges");
        Page<GameBadgeDTO> page = gameBadgeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /game-badges/:id} : get the "id" gameBadge.
     *
     * @param id the id of the gameBadgeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameBadgeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-badges/{id}")
    public ResponseEntity<GameBadgeDTO> getGameBadge(@PathVariable Long id) {
        log.debug("REST request to get GameBadge : {}", id);
        Optional<GameBadgeDTO> gameBadgeDTO = gameBadgeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameBadgeDTO);
    }

    /**
     * {@code DELETE  /game-badges/:id} : delete the "id" gameBadge.
     *
     * @param id the id of the gameBadgeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-badges/{id}")
    public ResponseEntity<Void> deleteGameBadge(@PathVariable Long id) {
        log.debug("REST request to delete GameBadge : {}", id);
        gameBadgeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
