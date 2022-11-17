package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.GameLevel;
import com.digicade.repository.GameLevelRepository;
import com.digicade.service.dto.GameLevelDTO;
import com.digicade.service.mapper.GameLevelMapper;
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
 * Integration tests for the {@link GameLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameLevelResourceIT {

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final String ENTITY_API_URL = "/api/game-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameLevelRepository gameLevelRepository;

    @Autowired
    private GameLevelMapper gameLevelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameLevelMockMvc;

    private GameLevel gameLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameLevel createEntity(EntityManager em) {
        GameLevel gameLevel = new GameLevel().level(DEFAULT_LEVEL).score(DEFAULT_SCORE);
        return gameLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameLevel createUpdatedEntity(EntityManager em) {
        GameLevel gameLevel = new GameLevel().level(UPDATED_LEVEL).score(UPDATED_SCORE);
        return gameLevel;
    }

    @BeforeEach
    public void initTest() {
        gameLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createGameLevel() throws Exception {
        int databaseSizeBeforeCreate = gameLevelRepository.findAll().size();
        // Create the GameLevel
        GameLevelDTO gameLevelDTO = gameLevelMapper.toDto(gameLevel);
        restGameLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameLevelDTO)))
            .andExpect(status().isCreated());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeCreate + 1);
        GameLevel testGameLevel = gameLevelList.get(gameLevelList.size() - 1);
        assertThat(testGameLevel.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testGameLevel.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    @Transactional
    void createGameLevelWithExistingId() throws Exception {
        // Create the GameLevel with an existing ID
        gameLevel.setId(1L);
        GameLevelDTO gameLevelDTO = gameLevelMapper.toDto(gameLevel);

        int databaseSizeBeforeCreate = gameLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameLevelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGameLevels() throws Exception {
        // Initialize the database
        gameLevelRepository.saveAndFlush(gameLevel);

        // Get all the gameLevelList
        restGameLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)));
    }

    @Test
    @Transactional
    void getGameLevel() throws Exception {
        // Initialize the database
        gameLevelRepository.saveAndFlush(gameLevel);

        // Get the gameLevel
        restGameLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, gameLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameLevel.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE));
    }

    @Test
    @Transactional
    void getNonExistingGameLevel() throws Exception {
        // Get the gameLevel
        restGameLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameLevel() throws Exception {
        // Initialize the database
        gameLevelRepository.saveAndFlush(gameLevel);

        int databaseSizeBeforeUpdate = gameLevelRepository.findAll().size();

        // Update the gameLevel
        GameLevel updatedGameLevel = gameLevelRepository.findById(gameLevel.getId()).get();
        // Disconnect from session so that the updates on updatedGameLevel are not directly saved in db
        em.detach(updatedGameLevel);
        updatedGameLevel.level(UPDATED_LEVEL).score(UPDATED_SCORE);
        GameLevelDTO gameLevelDTO = gameLevelMapper.toDto(updatedGameLevel);

        restGameLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameLevelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameLevelDTO))
            )
            .andExpect(status().isOk());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeUpdate);
        GameLevel testGameLevel = gameLevelList.get(gameLevelList.size() - 1);
        assertThat(testGameLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testGameLevel.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void putNonExistingGameLevel() throws Exception {
        int databaseSizeBeforeUpdate = gameLevelRepository.findAll().size();
        gameLevel.setId(count.incrementAndGet());

        // Create the GameLevel
        GameLevelDTO gameLevelDTO = gameLevelMapper.toDto(gameLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameLevelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameLevel() throws Exception {
        int databaseSizeBeforeUpdate = gameLevelRepository.findAll().size();
        gameLevel.setId(count.incrementAndGet());

        // Create the GameLevel
        GameLevelDTO gameLevelDTO = gameLevelMapper.toDto(gameLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameLevel() throws Exception {
        int databaseSizeBeforeUpdate = gameLevelRepository.findAll().size();
        gameLevel.setId(count.incrementAndGet());

        // Create the GameLevel
        GameLevelDTO gameLevelDTO = gameLevelMapper.toDto(gameLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameLevelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameLevelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameLevelWithPatch() throws Exception {
        // Initialize the database
        gameLevelRepository.saveAndFlush(gameLevel);

        int databaseSizeBeforeUpdate = gameLevelRepository.findAll().size();

        // Update the gameLevel using partial update
        GameLevel partialUpdatedGameLevel = new GameLevel();
        partialUpdatedGameLevel.setId(gameLevel.getId());

        partialUpdatedGameLevel.level(UPDATED_LEVEL);

        restGameLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameLevel))
            )
            .andExpect(status().isOk());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeUpdate);
        GameLevel testGameLevel = gameLevelList.get(gameLevelList.size() - 1);
        assertThat(testGameLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testGameLevel.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    @Transactional
    void fullUpdateGameLevelWithPatch() throws Exception {
        // Initialize the database
        gameLevelRepository.saveAndFlush(gameLevel);

        int databaseSizeBeforeUpdate = gameLevelRepository.findAll().size();

        // Update the gameLevel using partial update
        GameLevel partialUpdatedGameLevel = new GameLevel();
        partialUpdatedGameLevel.setId(gameLevel.getId());

        partialUpdatedGameLevel.level(UPDATED_LEVEL).score(UPDATED_SCORE);

        restGameLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameLevel))
            )
            .andExpect(status().isOk());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeUpdate);
        GameLevel testGameLevel = gameLevelList.get(gameLevelList.size() - 1);
        assertThat(testGameLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testGameLevel.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void patchNonExistingGameLevel() throws Exception {
        int databaseSizeBeforeUpdate = gameLevelRepository.findAll().size();
        gameLevel.setId(count.incrementAndGet());

        // Create the GameLevel
        GameLevelDTO gameLevelDTO = gameLevelMapper.toDto(gameLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameLevelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameLevel() throws Exception {
        int databaseSizeBeforeUpdate = gameLevelRepository.findAll().size();
        gameLevel.setId(count.incrementAndGet());

        // Create the GameLevel
        GameLevelDTO gameLevelDTO = gameLevelMapper.toDto(gameLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameLevel() throws Exception {
        int databaseSizeBeforeUpdate = gameLevelRepository.findAll().size();
        gameLevel.setId(count.incrementAndGet());

        // Create the GameLevel
        GameLevelDTO gameLevelDTO = gameLevelMapper.toDto(gameLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameLevelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameLevelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameLevel in the database
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameLevel() throws Exception {
        // Initialize the database
        gameLevelRepository.saveAndFlush(gameLevel);

        int databaseSizeBeforeDelete = gameLevelRepository.findAll().size();

        // Delete the gameLevel
        restGameLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameLevel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameLevel> gameLevelList = gameLevelRepository.findAll();
        assertThat(gameLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
