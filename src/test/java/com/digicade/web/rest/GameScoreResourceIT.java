package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.GameScore;
import com.digicade.repository.GameScoreRepository;
import com.digicade.service.dto.GameScoreDTO;
import com.digicade.service.mapper.GameScoreMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link GameScoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameScoreResourceIT {

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/game-scores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameScoreRepository gameScoreRepository;

    @Autowired
    private GameScoreMapper gameScoreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameScoreMockMvc;

    private GameScore gameScore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameScore createEntity(EntityManager em) {
        GameScore gameScore = new GameScore().score(DEFAULT_SCORE).date(DEFAULT_DATE);
        return gameScore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameScore createUpdatedEntity(EntityManager em) {
        GameScore gameScore = new GameScore().score(UPDATED_SCORE).date(UPDATED_DATE);
        return gameScore;
    }

    @BeforeEach
    public void initTest() {
        gameScore = createEntity(em);
    }

    @Test
    @Transactional
    void createGameScore() throws Exception {
        int databaseSizeBeforeCreate = gameScoreRepository.findAll().size();
        // Create the GameScore
        GameScoreDTO gameScoreDTO = gameScoreMapper.toDto(gameScore);
        restGameScoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameScoreDTO)))
            .andExpect(status().isCreated());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeCreate + 1);
        GameScore testGameScore = gameScoreList.get(gameScoreList.size() - 1);
        assertThat(testGameScore.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testGameScore.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createGameScoreWithExistingId() throws Exception {
        // Create the GameScore with an existing ID
        gameScore.setId(1L);
        GameScoreDTO gameScoreDTO = gameScoreMapper.toDto(gameScore);

        int databaseSizeBeforeCreate = gameScoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameScoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameScoreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGameScores() throws Exception {
        // Initialize the database
        gameScoreRepository.saveAndFlush(gameScore);

        // Get all the gameScoreList
        restGameScoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameScore.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getGameScore() throws Exception {
        // Initialize the database
        gameScoreRepository.saveAndFlush(gameScore);

        // Get the gameScore
        restGameScoreMockMvc
            .perform(get(ENTITY_API_URL_ID, gameScore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameScore.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGameScore() throws Exception {
        // Get the gameScore
        restGameScoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameScore() throws Exception {
        // Initialize the database
        gameScoreRepository.saveAndFlush(gameScore);

        int databaseSizeBeforeUpdate = gameScoreRepository.findAll().size();

        // Update the gameScore
        GameScore updatedGameScore = gameScoreRepository.findById(gameScore.getId()).get();
        // Disconnect from session so that the updates on updatedGameScore are not directly saved in db
        em.detach(updatedGameScore);
        updatedGameScore.score(UPDATED_SCORE).date(UPDATED_DATE);
        GameScoreDTO gameScoreDTO = gameScoreMapper.toDto(updatedGameScore);

        restGameScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameScoreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameScoreDTO))
            )
            .andExpect(status().isOk());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeUpdate);
        GameScore testGameScore = gameScoreList.get(gameScoreList.size() - 1);
        assertThat(testGameScore.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testGameScore.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingGameScore() throws Exception {
        int databaseSizeBeforeUpdate = gameScoreRepository.findAll().size();
        gameScore.setId(count.incrementAndGet());

        // Create the GameScore
        GameScoreDTO gameScoreDTO = gameScoreMapper.toDto(gameScore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameScoreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameScoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameScore() throws Exception {
        int databaseSizeBeforeUpdate = gameScoreRepository.findAll().size();
        gameScore.setId(count.incrementAndGet());

        // Create the GameScore
        GameScoreDTO gameScoreDTO = gameScoreMapper.toDto(gameScore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameScoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameScore() throws Exception {
        int databaseSizeBeforeUpdate = gameScoreRepository.findAll().size();
        gameScore.setId(count.incrementAndGet());

        // Create the GameScore
        GameScoreDTO gameScoreDTO = gameScoreMapper.toDto(gameScore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameScoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameScoreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameScoreWithPatch() throws Exception {
        // Initialize the database
        gameScoreRepository.saveAndFlush(gameScore);

        int databaseSizeBeforeUpdate = gameScoreRepository.findAll().size();

        // Update the gameScore using partial update
        GameScore partialUpdatedGameScore = new GameScore();
        partialUpdatedGameScore.setId(gameScore.getId());

        partialUpdatedGameScore.date(UPDATED_DATE);

        restGameScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameScore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameScore))
            )
            .andExpect(status().isOk());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeUpdate);
        GameScore testGameScore = gameScoreList.get(gameScoreList.size() - 1);
        assertThat(testGameScore.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testGameScore.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateGameScoreWithPatch() throws Exception {
        // Initialize the database
        gameScoreRepository.saveAndFlush(gameScore);

        int databaseSizeBeforeUpdate = gameScoreRepository.findAll().size();

        // Update the gameScore using partial update
        GameScore partialUpdatedGameScore = new GameScore();
        partialUpdatedGameScore.setId(gameScore.getId());

        partialUpdatedGameScore.score(UPDATED_SCORE).date(UPDATED_DATE);

        restGameScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameScore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameScore))
            )
            .andExpect(status().isOk());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeUpdate);
        GameScore testGameScore = gameScoreList.get(gameScoreList.size() - 1);
        assertThat(testGameScore.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testGameScore.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingGameScore() throws Exception {
        int databaseSizeBeforeUpdate = gameScoreRepository.findAll().size();
        gameScore.setId(count.incrementAndGet());

        // Create the GameScore
        GameScoreDTO gameScoreDTO = gameScoreMapper.toDto(gameScore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameScoreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameScoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameScore() throws Exception {
        int databaseSizeBeforeUpdate = gameScoreRepository.findAll().size();
        gameScore.setId(count.incrementAndGet());

        // Create the GameScore
        GameScoreDTO gameScoreDTO = gameScoreMapper.toDto(gameScore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameScoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameScore() throws Exception {
        int databaseSizeBeforeUpdate = gameScoreRepository.findAll().size();
        gameScore.setId(count.incrementAndGet());

        // Create the GameScore
        GameScoreDTO gameScoreDTO = gameScoreMapper.toDto(gameScore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameScoreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameScoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameScore in the database
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameScore() throws Exception {
        // Initialize the database
        gameScoreRepository.saveAndFlush(gameScore);

        int databaseSizeBeforeDelete = gameScoreRepository.findAll().size();

        // Delete the gameScore
        restGameScoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameScore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameScore> gameScoreList = gameScoreRepository.findAll();
        assertThat(gameScoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
