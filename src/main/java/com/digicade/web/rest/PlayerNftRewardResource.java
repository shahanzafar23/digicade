package com.digicade.web.rest;

import com.digicade.repository.PlayerNftRewardRepository;
import com.digicade.service.PlayerNftRewardService;
import com.digicade.service.dto.PlayerNftRewardDTO;
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
 * REST controller for managing {@link com.digicade.domain.PlayerNftReward}.
 */
@RestController
@RequestMapping("/api")
public class PlayerNftRewardResource {

    private final Logger log = LoggerFactory.getLogger(PlayerNftRewardResource.class);

    private static final String ENTITY_NAME = "playerNftReward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerNftRewardService playerNftRewardService;

    private final PlayerNftRewardRepository playerNftRewardRepository;

    public PlayerNftRewardResource(PlayerNftRewardService playerNftRewardService, PlayerNftRewardRepository playerNftRewardRepository) {
        this.playerNftRewardService = playerNftRewardService;
        this.playerNftRewardRepository = playerNftRewardRepository;
    }

    /**
     * {@code POST  /player-nft-rewards} : Create a new playerNftReward.
     *
     * @param playerNftRewardDTO the playerNftRewardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerNftRewardDTO, or with status {@code 400 (Bad Request)} if the playerNftReward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/player-nft-rewards")
    public ResponseEntity<PlayerNftRewardDTO> createPlayerNftReward(@RequestBody PlayerNftRewardDTO playerNftRewardDTO)
        throws URISyntaxException {
        log.debug("REST request to save PlayerNftReward : {}", playerNftRewardDTO);
        if (playerNftRewardDTO.getId() != null) {
            throw new BadRequestAlertException("A new playerNftReward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerNftRewardDTO result = playerNftRewardService.save(playerNftRewardDTO);
        return ResponseEntity
            .created(new URI("/api/player-nft-rewards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /player-nft-rewards/:id} : Updates an existing playerNftReward.
     *
     * @param id the id of the playerNftRewardDTO to save.
     * @param playerNftRewardDTO the playerNftRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerNftRewardDTO,
     * or with status {@code 400 (Bad Request)} if the playerNftRewardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerNftRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/player-nft-rewards/{id}")
    public ResponseEntity<PlayerNftRewardDTO> updatePlayerNftReward(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlayerNftRewardDTO playerNftRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PlayerNftReward : {}, {}", id, playerNftRewardDTO);
        if (playerNftRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerNftRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerNftRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlayerNftRewardDTO result = playerNftRewardService.update(playerNftRewardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playerNftRewardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /player-nft-rewards/:id} : Partial updates given fields of an existing playerNftReward, field will ignore if it is null
     *
     * @param id the id of the playerNftRewardDTO to save.
     * @param playerNftRewardDTO the playerNftRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerNftRewardDTO,
     * or with status {@code 400 (Bad Request)} if the playerNftRewardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the playerNftRewardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the playerNftRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/player-nft-rewards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlayerNftRewardDTO> partialUpdatePlayerNftReward(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlayerNftRewardDTO playerNftRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlayerNftReward partially : {}, {}", id, playerNftRewardDTO);
        if (playerNftRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerNftRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerNftRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayerNftRewardDTO> result = playerNftRewardService.partialUpdate(playerNftRewardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playerNftRewardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /player-nft-rewards} : get all the playerNftRewards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playerNftRewards in body.
     */
    @GetMapping("/player-nft-rewards")
    public ResponseEntity<List<PlayerNftRewardDTO>> getAllPlayerNftRewards(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PlayerNftRewards");
        Page<PlayerNftRewardDTO> page = playerNftRewardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /player-nft-rewards/:id} : get the "id" playerNftReward.
     *
     * @param id the id of the playerNftRewardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerNftRewardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/player-nft-rewards/{id}")
    public ResponseEntity<PlayerNftRewardDTO> getPlayerNftReward(@PathVariable Long id) {
        log.debug("REST request to get PlayerNftReward : {}", id);
        Optional<PlayerNftRewardDTO> playerNftRewardDTO = playerNftRewardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playerNftRewardDTO);
    }

    /**
     * {@code DELETE  /player-nft-rewards/:id} : delete the "id" playerNftReward.
     *
     * @param id the id of the playerNftRewardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/player-nft-rewards/{id}")
    public ResponseEntity<Void> deletePlayerNftReward(@PathVariable Long id) {
        log.debug("REST request to delete PlayerNftReward : {}", id);
        playerNftRewardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
