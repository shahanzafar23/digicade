package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.PlayerCouponReward;
import com.digicade.domain.enumeration.CouponStatus;
import com.digicade.repository.PlayerCouponRewardRepository;
import com.digicade.service.dto.PlayerCouponRewardDTO;
import com.digicade.service.mapper.PlayerCouponRewardMapper;
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
 * Integration tests for the {@link PlayerCouponRewardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayerCouponRewardResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final CouponStatus DEFAULT_STATUS = CouponStatus.REDEEMED;
    private static final CouponStatus UPDATED_STATUS = CouponStatus.REFUNDED;

    private static final String ENTITY_API_URL = "/api/player-coupon-rewards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayerCouponRewardRepository playerCouponRewardRepository;

    @Autowired
    private PlayerCouponRewardMapper playerCouponRewardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerCouponRewardMockMvc;

    private PlayerCouponReward playerCouponReward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerCouponReward createEntity(EntityManager em) {
        PlayerCouponReward playerCouponReward = new PlayerCouponReward().date(DEFAULT_DATE).status(DEFAULT_STATUS);
        return playerCouponReward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerCouponReward createUpdatedEntity(EntityManager em) {
        PlayerCouponReward playerCouponReward = new PlayerCouponReward().date(UPDATED_DATE).status(UPDATED_STATUS);
        return playerCouponReward;
    }

    @BeforeEach
    public void initTest() {
        playerCouponReward = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayerCouponReward() throws Exception {
        int databaseSizeBeforeCreate = playerCouponRewardRepository.findAll().size();
        // Create the PlayerCouponReward
        PlayerCouponRewardDTO playerCouponRewardDTO = playerCouponRewardMapper.toDto(playerCouponReward);
        restPlayerCouponRewardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerCouponRewardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerCouponReward testPlayerCouponReward = playerCouponRewardList.get(playerCouponRewardList.size() - 1);
        assertThat(testPlayerCouponReward.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPlayerCouponReward.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createPlayerCouponRewardWithExistingId() throws Exception {
        // Create the PlayerCouponReward with an existing ID
        playerCouponReward.setId(1L);
        PlayerCouponRewardDTO playerCouponRewardDTO = playerCouponRewardMapper.toDto(playerCouponReward);

        int databaseSizeBeforeCreate = playerCouponRewardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerCouponRewardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerCouponRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlayerCouponRewards() throws Exception {
        // Initialize the database
        playerCouponRewardRepository.saveAndFlush(playerCouponReward);

        // Get all the playerCouponRewardList
        restPlayerCouponRewardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerCouponReward.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPlayerCouponReward() throws Exception {
        // Initialize the database
        playerCouponRewardRepository.saveAndFlush(playerCouponReward);

        // Get the playerCouponReward
        restPlayerCouponRewardMockMvc
            .perform(get(ENTITY_API_URL_ID, playerCouponReward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playerCouponReward.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlayerCouponReward() throws Exception {
        // Get the playerCouponReward
        restPlayerCouponRewardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlayerCouponReward() throws Exception {
        // Initialize the database
        playerCouponRewardRepository.saveAndFlush(playerCouponReward);

        int databaseSizeBeforeUpdate = playerCouponRewardRepository.findAll().size();

        // Update the playerCouponReward
        PlayerCouponReward updatedPlayerCouponReward = playerCouponRewardRepository.findById(playerCouponReward.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerCouponReward are not directly saved in db
        em.detach(updatedPlayerCouponReward);
        updatedPlayerCouponReward.date(UPDATED_DATE).status(UPDATED_STATUS);
        PlayerCouponRewardDTO playerCouponRewardDTO = playerCouponRewardMapper.toDto(updatedPlayerCouponReward);

        restPlayerCouponRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerCouponRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerCouponRewardDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeUpdate);
        PlayerCouponReward testPlayerCouponReward = playerCouponRewardList.get(playerCouponRewardList.size() - 1);
        assertThat(testPlayerCouponReward.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPlayerCouponReward.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingPlayerCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = playerCouponRewardRepository.findAll().size();
        playerCouponReward.setId(count.incrementAndGet());

        // Create the PlayerCouponReward
        PlayerCouponRewardDTO playerCouponRewardDTO = playerCouponRewardMapper.toDto(playerCouponReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerCouponRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerCouponRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerCouponRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayerCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = playerCouponRewardRepository.findAll().size();
        playerCouponReward.setId(count.incrementAndGet());

        // Create the PlayerCouponReward
        PlayerCouponRewardDTO playerCouponRewardDTO = playerCouponRewardMapper.toDto(playerCouponReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerCouponRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerCouponRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayerCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = playerCouponRewardRepository.findAll().size();
        playerCouponReward.setId(count.incrementAndGet());

        // Create the PlayerCouponReward
        PlayerCouponRewardDTO playerCouponRewardDTO = playerCouponRewardMapper.toDto(playerCouponReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerCouponRewardMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerCouponRewardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayerCouponRewardWithPatch() throws Exception {
        // Initialize the database
        playerCouponRewardRepository.saveAndFlush(playerCouponReward);

        int databaseSizeBeforeUpdate = playerCouponRewardRepository.findAll().size();

        // Update the playerCouponReward using partial update
        PlayerCouponReward partialUpdatedPlayerCouponReward = new PlayerCouponReward();
        partialUpdatedPlayerCouponReward.setId(playerCouponReward.getId());

        partialUpdatedPlayerCouponReward.status(UPDATED_STATUS);

        restPlayerCouponRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerCouponReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerCouponReward))
            )
            .andExpect(status().isOk());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeUpdate);
        PlayerCouponReward testPlayerCouponReward = playerCouponRewardList.get(playerCouponRewardList.size() - 1);
        assertThat(testPlayerCouponReward.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPlayerCouponReward.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdatePlayerCouponRewardWithPatch() throws Exception {
        // Initialize the database
        playerCouponRewardRepository.saveAndFlush(playerCouponReward);

        int databaseSizeBeforeUpdate = playerCouponRewardRepository.findAll().size();

        // Update the playerCouponReward using partial update
        PlayerCouponReward partialUpdatedPlayerCouponReward = new PlayerCouponReward();
        partialUpdatedPlayerCouponReward.setId(playerCouponReward.getId());

        partialUpdatedPlayerCouponReward.date(UPDATED_DATE).status(UPDATED_STATUS);

        restPlayerCouponRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerCouponReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerCouponReward))
            )
            .andExpect(status().isOk());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeUpdate);
        PlayerCouponReward testPlayerCouponReward = playerCouponRewardList.get(playerCouponRewardList.size() - 1);
        assertThat(testPlayerCouponReward.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPlayerCouponReward.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPlayerCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = playerCouponRewardRepository.findAll().size();
        playerCouponReward.setId(count.incrementAndGet());

        // Create the PlayerCouponReward
        PlayerCouponRewardDTO playerCouponRewardDTO = playerCouponRewardMapper.toDto(playerCouponReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerCouponRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playerCouponRewardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerCouponRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayerCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = playerCouponRewardRepository.findAll().size();
        playerCouponReward.setId(count.incrementAndGet());

        // Create the PlayerCouponReward
        PlayerCouponRewardDTO playerCouponRewardDTO = playerCouponRewardMapper.toDto(playerCouponReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerCouponRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerCouponRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayerCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = playerCouponRewardRepository.findAll().size();
        playerCouponReward.setId(count.incrementAndGet());

        // Create the PlayerCouponReward
        PlayerCouponRewardDTO playerCouponRewardDTO = playerCouponRewardMapper.toDto(playerCouponReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerCouponRewardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerCouponRewardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerCouponReward in the database
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayerCouponReward() throws Exception {
        // Initialize the database
        playerCouponRewardRepository.saveAndFlush(playerCouponReward);

        int databaseSizeBeforeDelete = playerCouponRewardRepository.findAll().size();

        // Delete the playerCouponReward
        restPlayerCouponRewardMockMvc
            .perform(delete(ENTITY_API_URL_ID, playerCouponReward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerCouponReward> playerCouponRewardList = playerCouponRewardRepository.findAll();
        assertThat(playerCouponRewardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
