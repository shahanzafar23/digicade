package com.digicade.web.rest;

import com.digicade.repository.CouponImageRepository;
import com.digicade.service.CouponImageService;
import com.digicade.service.dto.CouponImageDTO;
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
 * REST controller for managing {@link com.digicade.domain.CouponImage}.
 */
@RestController
@RequestMapping("/api")
public class CouponImageResource {

    private final Logger log = LoggerFactory.getLogger(CouponImageResource.class);

    private static final String ENTITY_NAME = "couponImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CouponImageService couponImageService;

    private final CouponImageRepository couponImageRepository;

    public CouponImageResource(CouponImageService couponImageService, CouponImageRepository couponImageRepository) {
        this.couponImageService = couponImageService;
        this.couponImageRepository = couponImageRepository;
    }

    /**
     * {@code POST  /coupon-images} : Create a new couponImage.
     *
     * @param couponImageDTO the couponImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new couponImageDTO, or with status {@code 400 (Bad Request)} if the couponImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coupon-images")
    public ResponseEntity<CouponImageDTO> createCouponImage(@RequestBody CouponImageDTO couponImageDTO) throws URISyntaxException {
        log.debug("REST request to save CouponImage : {}", couponImageDTO);
        if (couponImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new couponImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CouponImageDTO result = couponImageService.save(couponImageDTO);
        return ResponseEntity
            .created(new URI("/api/coupon-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /coupon-images/:id} : Updates an existing couponImage.
     *
     * @param id the id of the couponImageDTO to save.
     * @param couponImageDTO the couponImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated couponImageDTO,
     * or with status {@code 400 (Bad Request)} if the couponImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the couponImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coupon-images/{id}")
    public ResponseEntity<CouponImageDTO> updateCouponImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CouponImageDTO couponImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CouponImage : {}, {}", id, couponImageDTO);
        if (couponImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, couponImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!couponImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CouponImageDTO result = couponImageService.update(couponImageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, couponImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /coupon-images/:id} : Partial updates given fields of an existing couponImage, field will ignore if it is null
     *
     * @param id the id of the couponImageDTO to save.
     * @param couponImageDTO the couponImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated couponImageDTO,
     * or with status {@code 400 (Bad Request)} if the couponImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the couponImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the couponImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/coupon-images/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CouponImageDTO> partialUpdateCouponImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CouponImageDTO couponImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CouponImage partially : {}, {}", id, couponImageDTO);
        if (couponImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, couponImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!couponImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CouponImageDTO> result = couponImageService.partialUpdate(couponImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, couponImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coupon-images} : get all the couponImages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of couponImages in body.
     */
    @GetMapping("/coupon-images")
    public ResponseEntity<List<CouponImageDTO>> getAllCouponImages(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CouponImages");
        Page<CouponImageDTO> page = couponImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coupon-images/:id} : get the "id" couponImage.
     *
     * @param id the id of the couponImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the couponImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/coupon-images/{id}")
    public ResponseEntity<CouponImageDTO> getCouponImage(@PathVariable Long id) {
        log.debug("REST request to get CouponImage : {}", id);
        Optional<CouponImageDTO> couponImageDTO = couponImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(couponImageDTO);
    }

    /**
     * {@code DELETE  /coupon-images/:id} : delete the "id" couponImage.
     *
     * @param id the id of the couponImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coupon-images/{id}")
    public ResponseEntity<Void> deleteCouponImage(@PathVariable Long id) {
        log.debug("REST request to delete CouponImage : {}", id);
        couponImageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
