package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.HighScore;
import com.digicade.repository.HighScoreRepository;
import com.digicade.service.dto.HighScoreDTO;
import com.digicade.service.mapper.HighScoreMapper;
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
 * Integration tests for the {@link HighScoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HighScoreResourceIT {

    private static final Integer DEFAULT_HIGHEST_SCORE = 1;
    private static final Integer UPDATED_HIGHEST_SCORE = 2;

    private static final String ENTITY_API_URL = "/api/high-scores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HighScoreRepository highScoreRepository;

    @Autowired
    private HighScoreMapper highScoreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHighScoreMockMvc;

    private HighScore highScore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HighScore createEntity(EntityManager em) {
        HighScore highScore = new HighScore().highestScore(DEFAULT_HIGHEST_SCORE);
        return highScore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HighScore createUpdatedEntity(EntityManager em) {
        HighScore highScore = new HighScore().highestScore(UPDATED_HIGHEST_SCORE);
        return highScore;
    }

    @BeforeEach
    public void initTest() {
        highScore = createEntity(em);
    }

    @Test
    @Transactional
    void createHighScore() throws Exception {
        int databaseSizeBeforeCreate = highScoreRepository.findAll().size();
        // Create the HighScore
        HighScoreDTO highScoreDTO = highScoreMapper.toDto(highScore);
        restHighScoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(highScoreDTO)))
            .andExpect(status().isCreated());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeCreate + 1);
        HighScore testHighScore = highScoreList.get(highScoreList.size() - 1);
        assertThat(testHighScore.getHighestScore()).isEqualTo(DEFAULT_HIGHEST_SCORE);
    }

    @Test
    @Transactional
    void createHighScoreWithExistingId() throws Exception {
        // Create the HighScore with an existing ID
        highScore.setId(1L);
        HighScoreDTO highScoreDTO = highScoreMapper.toDto(highScore);

        int databaseSizeBeforeCreate = highScoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHighScoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(highScoreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHighScores() throws Exception {
        // Initialize the database
        highScoreRepository.saveAndFlush(highScore);

        // Get all the highScoreList
        restHighScoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(highScore.getId().intValue())))
            .andExpect(jsonPath("$.[*].highestScore").value(hasItem(DEFAULT_HIGHEST_SCORE)));
    }

    @Test
    @Transactional
    void getHighScore() throws Exception {
        // Initialize the database
        highScoreRepository.saveAndFlush(highScore);

        // Get the highScore
        restHighScoreMockMvc
            .perform(get(ENTITY_API_URL_ID, highScore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(highScore.getId().intValue()))
            .andExpect(jsonPath("$.highestScore").value(DEFAULT_HIGHEST_SCORE));
    }

    @Test
    @Transactional
    void getNonExistingHighScore() throws Exception {
        // Get the highScore
        restHighScoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHighScore() throws Exception {
        // Initialize the database
        highScoreRepository.saveAndFlush(highScore);

        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();

        // Update the highScore
        HighScore updatedHighScore = highScoreRepository.findById(highScore.getId()).get();
        // Disconnect from session so that the updates on updatedHighScore are not directly saved in db
        em.detach(updatedHighScore);
        updatedHighScore.highestScore(UPDATED_HIGHEST_SCORE);
        HighScoreDTO highScoreDTO = highScoreMapper.toDto(updatedHighScore);

        restHighScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, highScoreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(highScoreDTO))
            )
            .andExpect(status().isOk());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
        HighScore testHighScore = highScoreList.get(highScoreList.size() - 1);
        assertThat(testHighScore.getHighestScore()).isEqualTo(UPDATED_HIGHEST_SCORE);
    }

    @Test
    @Transactional
    void putNonExistingHighScore() throws Exception {
        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();
        highScore.setId(count.incrementAndGet());

        // Create the HighScore
        HighScoreDTO highScoreDTO = highScoreMapper.toDto(highScore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHighScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, highScoreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(highScoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHighScore() throws Exception {
        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();
        highScore.setId(count.incrementAndGet());

        // Create the HighScore
        HighScoreDTO highScoreDTO = highScoreMapper.toDto(highScore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHighScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(highScoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHighScore() throws Exception {
        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();
        highScore.setId(count.incrementAndGet());

        // Create the HighScore
        HighScoreDTO highScoreDTO = highScoreMapper.toDto(highScore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHighScoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(highScoreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHighScoreWithPatch() throws Exception {
        // Initialize the database
        highScoreRepository.saveAndFlush(highScore);

        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();

        // Update the highScore using partial update
        HighScore partialUpdatedHighScore = new HighScore();
        partialUpdatedHighScore.setId(highScore.getId());

        partialUpdatedHighScore.highestScore(UPDATED_HIGHEST_SCORE);

        restHighScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHighScore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHighScore))
            )
            .andExpect(status().isOk());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
        HighScore testHighScore = highScoreList.get(highScoreList.size() - 1);
        assertThat(testHighScore.getHighestScore()).isEqualTo(UPDATED_HIGHEST_SCORE);
    }

    @Test
    @Transactional
    void fullUpdateHighScoreWithPatch() throws Exception {
        // Initialize the database
        highScoreRepository.saveAndFlush(highScore);

        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();

        // Update the highScore using partial update
        HighScore partialUpdatedHighScore = new HighScore();
        partialUpdatedHighScore.setId(highScore.getId());

        partialUpdatedHighScore.highestScore(UPDATED_HIGHEST_SCORE);

        restHighScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHighScore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHighScore))
            )
            .andExpect(status().isOk());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
        HighScore testHighScore = highScoreList.get(highScoreList.size() - 1);
        assertThat(testHighScore.getHighestScore()).isEqualTo(UPDATED_HIGHEST_SCORE);
    }

    @Test
    @Transactional
    void patchNonExistingHighScore() throws Exception {
        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();
        highScore.setId(count.incrementAndGet());

        // Create the HighScore
        HighScoreDTO highScoreDTO = highScoreMapper.toDto(highScore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHighScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, highScoreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(highScoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHighScore() throws Exception {
        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();
        highScore.setId(count.incrementAndGet());

        // Create the HighScore
        HighScoreDTO highScoreDTO = highScoreMapper.toDto(highScore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHighScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(highScoreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHighScore() throws Exception {
        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();
        highScore.setId(count.incrementAndGet());

        // Create the HighScore
        HighScoreDTO highScoreDTO = highScoreMapper.toDto(highScore);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHighScoreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(highScoreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHighScore() throws Exception {
        // Initialize the database
        highScoreRepository.saveAndFlush(highScore);

        int databaseSizeBeforeDelete = highScoreRepository.findAll().size();

        // Delete the highScore
        restHighScoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, highScore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
