package com.digicade.web.rest;

import com.digicade.repository.CouponRewardRepository;
import com.digicade.service.CouponRewardService;
import com.digicade.service.dto.CouponRewardDTO;
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
 * REST controller for managing {@link com.digicade.domain.CouponReward}.
 */
@RestController
@RequestMapping("/api")
public class CouponRewardResource {

    private final Logger log = LoggerFactory.getLogger(CouponRewardResource.class);

    private static final String ENTITY_NAME = "couponReward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CouponRewardService couponRewardService;

    private final CouponRewardRepository couponRewardRepository;

    public CouponRewardResource(CouponRewardService couponRewardService, CouponRewardRepository couponRewardRepository) {
        this.couponRewardService = couponRewardService;
        this.couponRewardRepository = couponRewardRepository;
    }

    /**
     * {@code POST  /coupon-rewards} : Create a new couponReward.
     *
     * @param couponRewardDTO the couponRewardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new couponRewardDTO, or with status {@code 400 (Bad Request)} if the couponReward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coupon-rewards")
    public ResponseEntity<CouponRewardDTO> createCouponReward(@RequestBody CouponRewardDTO couponRewardDTO) throws URISyntaxException {
        log.debug("REST request to save CouponReward : {}", couponRewardDTO);
        if (couponRewardDTO.getId() != null) {
            throw new BadRequestAlertException("A new couponReward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CouponRewardDTO result = couponRewardService.save(couponRewardDTO);
        return ResponseEntity
            .created(new URI("/api/coupon-rewards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /coupon-rewards/:id} : Updates an existing couponReward.
     *
     * @param id the id of the couponRewardDTO to save.
     * @param couponRewardDTO the couponRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated couponRewardDTO,
     * or with status {@code 400 (Bad Request)} if the couponRewardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the couponRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coupon-rewards/{id}")
    public ResponseEntity<CouponRewardDTO> updateCouponReward(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CouponRewardDTO couponRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CouponReward : {}, {}", id, couponRewardDTO);
        if (couponRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, couponRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!couponRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CouponRewardDTO result = couponRewardService.update(couponRewardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, couponRewardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /coupon-rewards/:id} : Partial updates given fields of an existing couponReward, field will ignore if it is null
     *
     * @param id the id of the couponRewardDTO to save.
     * @param couponRewardDTO the couponRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated couponRewardDTO,
     * or with status {@code 400 (Bad Request)} if the couponRewardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the couponRewardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the couponRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/coupon-rewards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CouponRewardDTO> partialUpdateCouponReward(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CouponRewardDTO couponRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CouponReward partially : {}, {}", id, couponRewardDTO);
        if (couponRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, couponRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!couponRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CouponRewardDTO> result = couponRewardService.partialUpdate(couponRewardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, couponRewardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coupon-rewards} : get all the couponRewards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of couponRewards in body.
     */
    @GetMapping("/coupon-rewards")
    public ResponseEntity<List<CouponRewardDTO>> getAllCouponRewards(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CouponRewards");
        Page<CouponRewardDTO> page = couponRewardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coupon-rewards/:id} : get the "id" couponReward.
     *
     * @param id the id of the couponRewardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the couponRewardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/coupon-rewards/{id}")
    public ResponseEntity<CouponRewardDTO> getCouponReward(@PathVariable Long id) {
        log.debug("REST request to get CouponReward : {}", id);
        Optional<CouponRewardDTO> couponRewardDTO = couponRewardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(couponRewardDTO);
    }

    /**
     * {@code DELETE  /coupon-rewards/:id} : delete the "id" couponReward.
     *
     * @param id the id of the couponRewardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coupon-rewards/{id}")
    public ResponseEntity<Void> deleteCouponReward(@PathVariable Long id) {
        log.debug("REST request to delete CouponReward : {}", id);
        couponRewardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
