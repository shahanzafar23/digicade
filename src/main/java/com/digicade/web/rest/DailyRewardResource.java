package com.digicade.web.rest;

import com.digicade.repository.DailyRewardRepository;
import com.digicade.service.DailyRewardService;
import com.digicade.service.dto.DailyRewardDTO;
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
 * REST controller for managing {@link com.digicade.domain.DailyReward}.
 */
@RestController
@RequestMapping("/api")
public class DailyRewardResource {

    private final Logger log = LoggerFactory.getLogger(DailyRewardResource.class);

    private static final String ENTITY_NAME = "dailyReward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DailyRewardService dailyRewardService;

    private final DailyRewardRepository dailyRewardRepository;

    public DailyRewardResource(DailyRewardService dailyRewardService, DailyRewardRepository dailyRewardRepository) {
        this.dailyRewardService = dailyRewardService;
        this.dailyRewardRepository = dailyRewardRepository;
    }

    /**
     * {@code POST  /daily-rewards} : Create a new dailyReward.
     *
     * @param dailyRewardDTO the dailyRewardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dailyRewardDTO, or with status {@code 400 (Bad Request)} if the dailyReward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/daily-rewards")
    public ResponseEntity<DailyRewardDTO> createDailyReward(@RequestBody DailyRewardDTO dailyRewardDTO) throws URISyntaxException {
        log.debug("REST request to save DailyReward : {}", dailyRewardDTO);
        if (dailyRewardDTO.getId() != null) {
            throw new BadRequestAlertException("A new dailyReward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DailyRewardDTO result = dailyRewardService.save(dailyRewardDTO);
        return ResponseEntity
            .created(new URI("/api/daily-rewards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /daily-rewards/:id} : Updates an existing dailyReward.
     *
     * @param id the id of the dailyRewardDTO to save.
     * @param dailyRewardDTO the dailyRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dailyRewardDTO,
     * or with status {@code 400 (Bad Request)} if the dailyRewardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dailyRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/daily-rewards/{id}")
    public ResponseEntity<DailyRewardDTO> updateDailyReward(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DailyRewardDTO dailyRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DailyReward : {}, {}", id, dailyRewardDTO);
        if (dailyRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dailyRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dailyRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DailyRewardDTO result = dailyRewardService.update(dailyRewardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dailyRewardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /daily-rewards/:id} : Partial updates given fields of an existing dailyReward, field will ignore if it is null
     *
     * @param id the id of the dailyRewardDTO to save.
     * @param dailyRewardDTO the dailyRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dailyRewardDTO,
     * or with status {@code 400 (Bad Request)} if the dailyRewardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dailyRewardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dailyRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/daily-rewards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DailyRewardDTO> partialUpdateDailyReward(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DailyRewardDTO dailyRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DailyReward partially : {}, {}", id, dailyRewardDTO);
        if (dailyRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dailyRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dailyRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DailyRewardDTO> result = dailyRewardService.partialUpdate(dailyRewardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dailyRewardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /daily-rewards} : get all the dailyRewards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dailyRewards in body.
     */
    @GetMapping("/daily-rewards")
    public ResponseEntity<List<DailyRewardDTO>> getAllDailyRewards(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DailyRewards");
        Page<DailyRewardDTO> page = dailyRewardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /daily-rewards/:id} : get the "id" dailyReward.
     *
     * @param id the id of the dailyRewardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dailyRewardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/daily-rewards/{id}")
    public ResponseEntity<DailyRewardDTO> getDailyReward(@PathVariable Long id) {
        log.debug("REST request to get DailyReward : {}", id);
        Optional<DailyRewardDTO> dailyRewardDTO = dailyRewardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dailyRewardDTO);
    }

    /**
     * {@code DELETE  /daily-rewards/:id} : delete the "id" dailyReward.
     *
     * @param id the id of the dailyRewardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/daily-rewards/{id}")
    public ResponseEntity<Void> deleteDailyReward(@PathVariable Long id) {
        log.debug("REST request to delete DailyReward : {}", id);
        dailyRewardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
