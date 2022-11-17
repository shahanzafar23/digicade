package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.GameBadge;
import com.digicade.repository.GameBadgeRepository;
import com.digicade.service.dto.GameBadgeDTO;
import com.digicade.service.mapper.GameBadgeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GameBadgeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameBadgeResourceIT {

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/game-badges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameBadgeRepository gameBadgeRepository;

    @Autowired
    private GameBadgeMapper gameBadgeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameBadgeMockMvc;

    private GameBadge gameBadge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameBadge createEntity(EntityManager em) {
        GameBadge gameBadge = new GameBadge().logoUrl(DEFAULT_LOGO_URL);
        return gameBadge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameBadge createUpdatedEntity(EntityManager em) {
        GameBadge gameBadge = new GameBadge().logoUrl(UPDATED_LOGO_URL);
        return gameBadge;
    }

    @BeforeEach
    public void initTest() {
        gameBadge = createEntity(em);
    }

    @Test
    @Transactional
    void createGameBadge() throws Exception {
        int databaseSizeBeforeCreate = gameBadgeRepository.findAll().size();
        // Create the GameBadge
        GameBadgeDTO gameBadgeDTO = gameBadgeMapper.toDto(gameBadge);
        restGameBadgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameBadgeDTO)))
            .andExpect(status().isCreated());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeCreate + 1);
        GameBadge testGameBadge = gameBadgeList.get(gameBadgeList.size() - 1);
        assertThat(testGameBadge.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
    }

    @Test
    @Transactional
    void createGameBadgeWithExistingId() throws Exception {
        // Create the GameBadge with an existing ID
        gameBadge.setId(1L);
        GameBadgeDTO gameBadgeDTO = gameBadgeMapper.toDto(gameBadge);

        int databaseSizeBeforeCreate = gameBadgeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameBadgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameBadgeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGameBadges() throws Exception {
        // Initialize the database
        gameBadgeRepository.saveAndFlush(gameBadge);

        // Get all the gameBadgeList
        restGameBadgeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameBadge.getId().intValue())))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)));
    }

    @Test
    @Transactional
    void getGameBadge() throws Exception {
        // Initialize the database
        gameBadgeRepository.saveAndFlush(gameBadge);

        // Get the gameBadge
        restGameBadgeMockMvc
            .perform(get(ENTITY_API_URL_ID, gameBadge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameBadge.getId().intValue()))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL));
    }

    @Test
    @Transactional
    void getNonExistingGameBadge() throws Exception {
        // Get the gameBadge
        restGameBadgeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameBadge() throws Exception {
        // Initialize the database
        gameBadgeRepository.saveAndFlush(gameBadge);

        int databaseSizeBeforeUpdate = gameBadgeRepository.findAll().size();

        // Update the gameBadge
        GameBadge updatedGameBadge = gameBadgeRepository.findById(gameBadge.getId()).get();
        // Disconnect from session so that the updates on updatedGameBadge are not directly saved in db
        em.detach(updatedGameBadge);
        updatedGameBadge.logoUrl(UPDATED_LOGO_URL);
        GameBadgeDTO gameBadgeDTO = gameBadgeMapper.toDto(updatedGameBadge);

        restGameBadgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameBadgeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameBadgeDTO))
            )
            .andExpect(status().isOk());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeUpdate);
        GameBadge testGameBadge = gameBadgeList.get(gameBadgeList.size() - 1);
        assertThat(testGameBadge.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void putNonExistingGameBadge() throws Exception {
        int databaseSizeBeforeUpdate = gameBadgeRepository.findAll().size();
        gameBadge.setId(count.incrementAndGet());

        // Create the GameBadge
        GameBadgeDTO gameBadgeDTO = gameBadgeMapper.toDto(gameBadge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameBadgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameBadgeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameBadgeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameBadge() throws Exception {
        int databaseSizeBeforeUpdate = gameBadgeRepository.findAll().size();
        gameBadge.setId(count.incrementAndGet());

        // Create the GameBadge
        GameBadgeDTO gameBadgeDTO = gameBadgeMapper.toDto(gameBadge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameBadgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameBadgeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameBadge() throws Exception {
        int databaseSizeBeforeUpdate = gameBadgeRepository.findAll().size();
        gameBadge.setId(count.incrementAndGet());

        // Create the GameBadge
        GameBadgeDTO gameBadgeDTO = gameBadgeMapper.toDto(gameBadge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameBadgeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameBadgeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameBadgeWithPatch() throws Exception {
        // Initialize the database
        gameBadgeRepository.saveAndFlush(gameBadge);

        int databaseSizeBeforeUpdate = gameBadgeRepository.findAll().size();

        // Update the gameBadge using partial update
        GameBadge partialUpdatedGameBadge = new GameBadge();
        partialUpdatedGameBadge.setId(gameBadge.getId());

        partialUpdatedGameBadge.logoUrl(UPDATED_LOGO_URL);

        restGameBadgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameBadge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameBadge))
            )
            .andExpect(status().isOk());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeUpdate);
        GameBadge testGameBadge = gameBadgeList.get(gameBadgeList.size() - 1);
        assertThat(testGameBadge.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void fullUpdateGameBadgeWithPatch() throws Exception {
        // Initialize the database
        gameBadgeRepository.saveAndFlush(gameBadge);

        int databaseSizeBeforeUpdate = gameBadgeRepository.findAll().size();

        // Update the gameBadge using partial update
        GameBadge partialUpdatedGameBadge = new GameBadge();
        partialUpdatedGameBadge.setId(gameBadge.getId());

        partialUpdatedGameBadge.logoUrl(UPDATED_LOGO_URL);

        restGameBadgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameBadge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameBadge))
            )
            .andExpect(status().isOk());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeUpdate);
        GameBadge testGameBadge = gameBadgeList.get(gameBadgeList.size() - 1);
        assertThat(testGameBadge.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void patchNonExistingGameBadge() throws Exception {
        int databaseSizeBeforeUpdate = gameBadgeRepository.findAll().size();
        gameBadge.setId(count.incrementAndGet());

        // Create the GameBadge
        GameBadgeDTO gameBadgeDTO = gameBadgeMapper.toDto(gameBadge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameBadgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameBadgeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameBadgeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameBadge() throws Exception {
        int databaseSizeBeforeUpdate = gameBadgeRepository.findAll().size();
        gameBadge.setId(count.incrementAndGet());

        // Create the GameBadge
        GameBadgeDTO gameBadgeDTO = gameBadgeMapper.toDto(gameBadge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameBadgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameBadgeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameBadge() throws Exception {
        int databaseSizeBeforeUpdate = gameBadgeRepository.findAll().size();
        gameBadge.setId(count.incrementAndGet());

        // Create the GameBadge
        GameBadgeDTO gameBadgeDTO = gameBadgeMapper.toDto(gameBadge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameBadgeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameBadgeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameBadge in the database
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameBadge() throws Exception {
        // Initialize the database
        gameBadgeRepository.saveAndFlush(gameBadge);

        int databaseSizeBeforeDelete = gameBadgeRepository.findAll().size();

        // Delete the gameBadge
        restGameBadgeMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameBadge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameBadge> gameBadgeList = gameBadgeRepository.findAll();
        assertThat(gameBadgeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
