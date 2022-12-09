package com.digicade.web.rest;

import com.digicade.domain.DigiUser;
import com.digicade.repository.DigiUserRepository;
import com.digicade.service.DigiUserService;
import com.digicade.service.dto.DigiUserDTO;
import com.digicade.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.digicade.domain.DigiUser}.
 */
@RestController
@RequestMapping("/api")
public class DigiUserResource {

    private final Logger log = LoggerFactory.getLogger(DigiUserResource.class);

    private static final String ENTITY_NAME = "digiUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DigiUserService digiUserService;

    private final DigiUserRepository digiUserRepository;

    public DigiUserResource(DigiUserService digiUserService, DigiUserRepository digiUserRepository) {
        this.digiUserService = digiUserService;
        this.digiUserRepository = digiUserRepository;
    }

    /**
     * {@code POST  /digi-users} : Create a new digiUser.
     *
     * @param digiUserDTO the digiUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new digiUserDTO, or with status {@code 400 (Bad Request)} if the digiUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/digi-users")
    public ResponseEntity<DigiUserDTO> createDigiUser(@RequestBody DigiUserDTO digiUserDTO) throws URISyntaxException {
        log.debug("REST request to save DigiUser : {}", digiUserDTO);
        if (digiUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new digiUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DigiUserDTO result = digiUserService.save(digiUserDTO);
        return ResponseEntity
            .created(new URI("/api/digi-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/digiusers")
    public ResponseEntity<DigiUser> createDigiUser2(@RequestBody DigiUser user) throws URISyntaxException {
        log.debug("REST request to save DigiUser : {}", user);

        DigiUser save = digiUserRepository.save(user);

        return new ResponseEntity<>(save, HttpStatus.OK);
    }

    /**
     * {@code PUT  /digi-users/:id} : Updates an existing digiUser.
     *
     * @param id the id of the digiUserDTO to save.
     * @param digiUserDTO the digiUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated digiUserDTO,
     * or with status {@code 400 (Bad Request)} if the digiUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the digiUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/digi-users/{id}")
    public ResponseEntity<DigiUserDTO> updateDigiUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DigiUserDTO digiUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DigiUser : {}, {}", id, digiUserDTO);
        if (digiUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, digiUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!digiUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DigiUserDTO result = digiUserService.update(digiUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, digiUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /digi-users/:id} : Partial updates given fields of an existing digiUser, field will ignore if it is null
     *
     * @param id the id of the digiUserDTO to save.
     * @param digiUserDTO the digiUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated digiUserDTO,
     * or with status {@code 400 (Bad Request)} if the digiUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the digiUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the digiUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/digi-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DigiUserDTO> partialUpdateDigiUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DigiUserDTO digiUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DigiUser partially : {}, {}", id, digiUserDTO);
        if (digiUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, digiUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!digiUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DigiUserDTO> result = digiUserService.partialUpdate(digiUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, digiUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /digi-users} : get all the digiUsers.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of digiUsers in body.
     */
    @GetMapping("/digi-users")
    public ResponseEntity<List<DigiUserDTO>> getAllDigiUsers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("player-is-null".equals(filter)) {
            log.debug("REST request to get all DigiUsers where player is null");
            return new ResponseEntity<>(digiUserService.findAllWherePlayerIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of DigiUsers");
        Page<DigiUserDTO> page = digiUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /digi-users/:id} : get the "id" digiUser.
     *
     * @param id the id of the digiUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the digiUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/digi-users/{id}")
    public ResponseEntity<DigiUserDTO> getDigiUser(@PathVariable Long id) {
        log.debug("REST request to get DigiUser : {}", id);
        Optional<DigiUserDTO> digiUserDTO = digiUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(digiUserDTO);
    }

    @GetMapping("/digiusers/{id}")
    public ResponseEntity<DigiUser> getDigiUserById(@PathVariable Long id) {
        log.debug("REST request to get DigiUser : {}", id);
        DigiUser digiUser = digiUserService.findDigiUsers(id);
        return new ResponseEntity<>(digiUser, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /digi-users/:id} : delete the "id" digiUser.
     *
     * @param id the id of the digiUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/digi-users/{id}")
    public ResponseEntity<Void> deleteDigiUser(@PathVariable Long id) {
        log.debug("REST request to delete DigiUser : {}", id);
        digiUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
