package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.PlayerNftReward;
import com.digicade.repository.PlayerNftRewardRepository;
import com.digicade.service.dto.PlayerNftRewardDTO;
import com.digicade.service.mapper.PlayerNftRewardMapper;
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
 * Integration tests for the {@link PlayerNftRewardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayerNftRewardResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/player-nft-rewards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayerNftRewardRepository playerNftRewardRepository;

    @Autowired
    private PlayerNftRewardMapper playerNftRewardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerNftRewardMockMvc;

    private PlayerNftReward playerNftReward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerNftReward createEntity(EntityManager em) {
        PlayerNftReward playerNftReward = new PlayerNftReward().date(DEFAULT_DATE);
        return playerNftReward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerNftReward createUpdatedEntity(EntityManager em) {
        PlayerNftReward playerNftReward = new PlayerNftReward().date(UPDATED_DATE);
        return playerNftReward;
    }

    @BeforeEach
    public void initTest() {
        playerNftReward = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayerNftReward() throws Exception {
        int databaseSizeBeforeCreate = playerNftRewardRepository.findAll().size();
        // Create the PlayerNftReward
        PlayerNftRewardDTO playerNftRewardDTO = playerNftRewardMapper.toDto(playerNftReward);
        restPlayerNftRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerNftRewardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerNftReward testPlayerNftReward = playerNftRewardList.get(playerNftRewardList.size() - 1);
        assertThat(testPlayerNftReward.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createPlayerNftRewardWithExistingId() throws Exception {
        // Create the PlayerNftReward with an existing ID
        playerNftReward.setId(1L);
        PlayerNftRewardDTO playerNftRewardDTO = playerNftRewardMapper.toDto(playerNftReward);

        int databaseSizeBeforeCreate = playerNftRewardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerNftRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerNftRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlayerNftRewards() throws Exception {
        // Initialize the database
        playerNftRewardRepository.saveAndFlush(playerNftReward);

        // Get all the playerNftRewardList
        restPlayerNftRewardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerNftReward.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getPlayerNftReward() throws Exception {
        // Initialize the database
        playerNftRewardRepository.saveAndFlush(playerNftReward);

        // Get the playerNftReward
        restPlayerNftRewardMockMvc
            .perform(get(ENTITY_API_URL_ID, playerNftReward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playerNftReward.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlayerNftReward() throws Exception {
        // Get the playerNftReward
        restPlayerNftRewardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlayerNftReward() throws Exception {
        // Initialize the database
        playerNftRewardRepository.saveAndFlush(playerNftReward);

        int databaseSizeBeforeUpdate = playerNftRewardRepository.findAll().size();

        // Update the playerNftReward
        PlayerNftReward updatedPlayerNftReward = playerNftRewardRepository.findById(playerNftReward.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerNftReward are not directly saved in db
        em.detach(updatedPlayerNftReward);
        updatedPlayerNftReward.date(UPDATED_DATE);
        PlayerNftRewardDTO playerNftRewardDTO = playerNftRewardMapper.toDto(updatedPlayerNftReward);

        restPlayerNftRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerNftRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerNftRewardDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeUpdate);
        PlayerNftReward testPlayerNftReward = playerNftRewardList.get(playerNftRewardList.size() - 1);
        assertThat(testPlayerNftReward.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPlayerNftReward() throws Exception {
        int databaseSizeBeforeUpdate = playerNftRewardRepository.findAll().size();
        playerNftReward.setId(count.incrementAndGet());

        // Create the PlayerNftReward
        PlayerNftRewardDTO playerNftRewardDTO = playerNftRewardMapper.toDto(playerNftReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerNftRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerNftRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerNftRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayerNftReward() throws Exception {
        int databaseSizeBeforeUpdate = playerNftRewardRepository.findAll().size();
        playerNftReward.setId(count.incrementAndGet());

        // Create the PlayerNftReward
        PlayerNftRewardDTO playerNftRewardDTO = playerNftRewardMapper.toDto(playerNftReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerNftRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerNftRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayerNftReward() throws Exception {
        int databaseSizeBeforeUpdate = playerNftRewardRepository.findAll().size();
        playerNftReward.setId(count.incrementAndGet());

        // Create the PlayerNftReward
        PlayerNftRewardDTO playerNftRewardDTO = playerNftRewardMapper.toDto(playerNftReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerNftRewardMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerNftRewardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayerNftRewardWithPatch() throws Exception {
        // Initialize the database
        playerNftRewardRepository.saveAndFlush(playerNftReward);

        int databaseSizeBeforeUpdate = playerNftRewardRepository.findAll().size();

        // Update the playerNftReward using partial update
        PlayerNftReward partialUpdatedPlayerNftReward = new PlayerNftReward();
        partialUpdatedPlayerNftReward.setId(playerNftReward.getId());

        partialUpdatedPlayerNftReward.date(UPDATED_DATE);

        restPlayerNftRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerNftReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerNftReward))
            )
            .andExpect(status().isOk());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeUpdate);
        PlayerNftReward testPlayerNftReward = playerNftRewardList.get(playerNftRewardList.size() - 1);
        assertThat(testPlayerNftReward.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePlayerNftRewardWithPatch() throws Exception {
        // Initialize the database
        playerNftRewardRepository.saveAndFlush(playerNftReward);

        int databaseSizeBeforeUpdate = playerNftRewardRepository.findAll().size();

        // Update the playerNftReward using partial update
        PlayerNftReward partialUpdatedPlayerNftReward = new PlayerNftReward();
        partialUpdatedPlayerNftReward.setId(playerNftReward.getId());

        partialUpdatedPlayerNftReward.date(UPDATED_DATE);

        restPlayerNftRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerNftReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerNftReward))
            )
            .andExpect(status().isOk());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeUpdate);
        PlayerNftReward testPlayerNftReward = playerNftRewardList.get(playerNftRewardList.size() - 1);
        assertThat(testPlayerNftReward.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPlayerNftReward() throws Exception {
        int databaseSizeBeforeUpdate = playerNftRewardRepository.findAll().size();
        playerNftReward.setId(count.incrementAndGet());

        // Create the PlayerNftReward
        PlayerNftRewardDTO playerNftRewardDTO = playerNftRewardMapper.toDto(playerNftReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerNftRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playerNftRewardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerNftRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayerNftReward() throws Exception {
        int databaseSizeBeforeUpdate = playerNftRewardRepository.findAll().size();
        playerNftReward.setId(count.incrementAndGet());

        // Create the PlayerNftReward
        PlayerNftRewardDTO playerNftRewardDTO = playerNftRewardMapper.toDto(playerNftReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerNftRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerNftRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayerNftReward() throws Exception {
        int databaseSizeBeforeUpdate = playerNftRewardRepository.findAll().size();
        playerNftReward.setId(count.incrementAndGet());

        // Create the PlayerNftReward
        PlayerNftRewardDTO playerNftRewardDTO = playerNftRewardMapper.toDto(playerNftReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerNftRewardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerNftRewardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerNftReward in the database
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayerNftReward() throws Exception {
        // Initialize the database
        playerNftRewardRepository.saveAndFlush(playerNftReward);

        int databaseSizeBeforeDelete = playerNftRewardRepository.findAll().size();

        // Delete the playerNftReward
        restPlayerNftRewardMockMvc
            .perform(delete(ENTITY_API_URL_ID, playerNftReward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerNftReward> playerNftRewardList = playerNftRewardRepository.findAll();
        assertThat(playerNftRewardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
