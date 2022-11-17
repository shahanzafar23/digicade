package com.digicade.web.rest;

import com.digicade.repository.CoinPackageRepository;
import com.digicade.service.CoinPackageService;
import com.digicade.service.dto.CoinPackageDTO;
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
 * REST controller for managing {@link com.digicade.domain.CoinPackage}.
 */
@RestController
@RequestMapping("/api")
public class CoinPackageResource {

    private final Logger log = LoggerFactory.getLogger(CoinPackageResource.class);

    private static final String ENTITY_NAME = "coinPackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoinPackageService coinPackageService;

    private final CoinPackageRepository coinPackageRepository;

    public CoinPackageResource(CoinPackageService coinPackageService, CoinPackageRepository coinPackageRepository) {
        this.coinPackageService = coinPackageService;
        this.coinPackageRepository = coinPackageRepository;
    }

    /**
     * {@code POST  /coin-packages} : Create a new coinPackage.
     *
     * @param coinPackageDTO the coinPackageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coinPackageDTO, or with status {@code 400 (Bad Request)} if the coinPackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coin-packages")
    public ResponseEntity<CoinPackageDTO> createCoinPackage(@RequestBody CoinPackageDTO coinPackageDTO) throws URISyntaxException {
        log.debug("REST request to save CoinPackage : {}", coinPackageDTO);
        if (coinPackageDTO.getId() != null) {
            throw new BadRequestAlertException("A new coinPackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoinPackageDTO result = coinPackageService.save(coinPackageDTO);
        return ResponseEntity
            .created(new URI("/api/coin-packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /coin-packages/:id} : Updates an existing coinPackage.
     *
     * @param id the id of the coinPackageDTO to save.
     * @param coinPackageDTO the coinPackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coinPackageDTO,
     * or with status {@code 400 (Bad Request)} if the coinPackageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coinPackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coin-packages/{id}")
    public ResponseEntity<CoinPackageDTO> updateCoinPackage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoinPackageDTO coinPackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CoinPackage : {}, {}", id, coinPackageDTO);
        if (coinPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coinPackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coinPackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CoinPackageDTO result = coinPackageService.update(coinPackageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, coinPackageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /coin-packages/:id} : Partial updates given fields of an existing coinPackage, field will ignore if it is null
     *
     * @param id the id of the coinPackageDTO to save.
     * @param coinPackageDTO the coinPackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coinPackageDTO,
     * or with status {@code 400 (Bad Request)} if the coinPackageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the coinPackageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the coinPackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/coin-packages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CoinPackageDTO> partialUpdateCoinPackage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoinPackageDTO coinPackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CoinPackage partially : {}, {}", id, coinPackageDTO);
        if (coinPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coinPackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coinPackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoinPackageDTO> result = coinPackageService.partialUpdate(coinPackageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, coinPackageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coin-packages} : get all the coinPackages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coinPackages in body.
     */
    @GetMapping("/coin-packages")
    public ResponseEntity<List<CoinPackageDTO>> getAllCoinPackages(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CoinPackages");
        Page<CoinPackageDTO> page = coinPackageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coin-packages/:id} : get the "id" coinPackage.
     *
     * @param id the id of the coinPackageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coinPackageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/coin-packages/{id}")
    public ResponseEntity<CoinPackageDTO> getCoinPackage(@PathVariable Long id) {
        log.debug("REST request to get CoinPackage : {}", id);
        Optional<CoinPackageDTO> coinPackageDTO = coinPackageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coinPackageDTO);
    }

    /**
     * {@code DELETE  /coin-packages/:id} : delete the "id" coinPackage.
     *
     * @param id the id of the coinPackageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coin-packages/{id}")
    public ResponseEntity<Void> deleteCoinPackage(@PathVariable Long id) {
        log.debug("REST request to delete CoinPackage : {}", id);
        coinPackageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
