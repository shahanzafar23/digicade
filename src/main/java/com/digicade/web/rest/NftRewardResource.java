package com.digicade.web.rest;

import com.digicade.repository.NftRewardRepository;
import com.digicade.service.NftRewardService;
import com.digicade.service.dto.NftRewardDTO;
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
 * REST controller for managing {@link com.digicade.domain.NftReward}.
 */
@RestController
@RequestMapping("/api")
public class NftRewardResource {

    private final Logger log = LoggerFactory.getLogger(NftRewardResource.class);

    private static final String ENTITY_NAME = "nftReward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NftRewardService nftRewardService;

    private final NftRewardRepository nftRewardRepository;

    public NftRewardResource(NftRewardService nftRewardService, NftRewardRepository nftRewardRepository) {
        this.nftRewardService = nftRewardService;
        this.nftRewardRepository = nftRewardRepository;
    }

    /**
     * {@code POST  /nft-rewards} : Create a new nftReward.
     *
     * @param nftRewardDTO the nftRewardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nftRewardDTO, or with status {@code 400 (Bad Request)} if the nftReward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nft-rewards")
    public ResponseEntity<NftRewardDTO> createNftReward(@RequestBody NftRewardDTO nftRewardDTO) throws URISyntaxException {
        log.debug("REST request to save NftReward : {}", nftRewardDTO);
        if (nftRewardDTO.getId() != null) {
            throw new BadRequestAlertException("A new nftReward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NftRewardDTO result = nftRewardService.save(nftRewardDTO);
        return ResponseEntity
            .created(new URI("/api/nft-rewards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nft-rewards/:id} : Updates an existing nftReward.
     *
     * @param id the id of the nftRewardDTO to save.
     * @param nftRewardDTO the nftRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nftRewardDTO,
     * or with status {@code 400 (Bad Request)} if the nftRewardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nftRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nft-rewards/{id}")
    public ResponseEntity<NftRewardDTO> updateNftReward(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NftRewardDTO nftRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NftReward : {}, {}", id, nftRewardDTO);
        if (nftRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nftRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nftRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NftRewardDTO result = nftRewardService.update(nftRewardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nftRewardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nft-rewards/:id} : Partial updates given fields of an existing nftReward, field will ignore if it is null
     *
     * @param id the id of the nftRewardDTO to save.
     * @param nftRewardDTO the nftRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nftRewardDTO,
     * or with status {@code 400 (Bad Request)} if the nftRewardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nftRewardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nftRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nft-rewards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NftRewardDTO> partialUpdateNftReward(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NftRewardDTO nftRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NftReward partially : {}, {}", id, nftRewardDTO);
        if (nftRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nftRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nftRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NftRewardDTO> result = nftRewardService.partialUpdate(nftRewardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nftRewardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nft-rewards} : get all the nftRewards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nftRewards in body.
     */
    @GetMapping("/nft-rewards")
    public ResponseEntity<List<NftRewardDTO>> getAllNftRewards(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of NftRewards");
        Page<NftRewardDTO> page = nftRewardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nft-rewards/:id} : get the "id" nftReward.
     *
     * @param id the id of the nftRewardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nftRewardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nft-rewards/{id}")
    public ResponseEntity<NftRewardDTO> getNftReward(@PathVariable Long id) {
        log.debug("REST request to get NftReward : {}", id);
        Optional<NftRewardDTO> nftRewardDTO = nftRewardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nftRewardDTO);
    }

    /**
     * {@code DELETE  /nft-rewards/:id} : delete the "id" nftReward.
     *
     * @param id the id of the nftRewardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nft-rewards/{id}")
    public ResponseEntity<Void> deleteNftReward(@PathVariable Long id) {
        log.debug("REST request to delete NftReward : {}", id);
        nftRewardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
