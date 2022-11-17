package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.DailyReward;
import com.digicade.domain.enumeration.RewardType;
import com.digicade.repository.DailyRewardRepository;
import com.digicade.service.dto.DailyRewardDTO;
import com.digicade.service.mapper.DailyRewardMapper;
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
 * Integration tests for the {@link DailyRewardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DailyRewardResourceIT {

    private static final String DEFAULT_TIME = "AAAAAAAAAA";
    private static final String UPDATED_TIME = "BBBBBBBBBB";

    private static final RewardType DEFAULT_REWARD_TYPE = RewardType.TIX;
    private static final RewardType UPDATED_REWARD_TYPE = RewardType.COMP;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String ENTITY_API_URL = "/api/daily-rewards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DailyRewardRepository dailyRewardRepository;

    @Autowired
    private DailyRewardMapper dailyRewardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDailyRewardMockMvc;

    private DailyReward dailyReward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyReward createEntity(EntityManager em) {
        DailyReward dailyReward = new DailyReward().time(DEFAULT_TIME).rewardType(DEFAULT_REWARD_TYPE).amount(DEFAULT_AMOUNT);
        return dailyReward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyReward createUpdatedEntity(EntityManager em) {
        DailyReward dailyReward = new DailyReward().time(UPDATED_TIME).rewardType(UPDATED_REWARD_TYPE).amount(UPDATED_AMOUNT);
        return dailyReward;
    }

    @BeforeEach
    public void initTest() {
        dailyReward = createEntity(em);
    }

    @Test
    @Transactional
    void createDailyReward() throws Exception {
        int databaseSizeBeforeCreate = dailyRewardRepository.findAll().size();
        // Create the DailyReward
        DailyRewardDTO dailyRewardDTO = dailyRewardMapper.toDto(dailyReward);
        restDailyRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyRewardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeCreate + 1);
        DailyReward testDailyReward = dailyRewardList.get(dailyRewardList.size() - 1);
        assertThat(testDailyReward.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testDailyReward.getRewardType()).isEqualTo(DEFAULT_REWARD_TYPE);
        assertThat(testDailyReward.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void createDailyRewardWithExistingId() throws Exception {
        // Create the DailyReward with an existing ID
        dailyReward.setId(1L);
        DailyRewardDTO dailyRewardDTO = dailyRewardMapper.toDto(dailyReward);

        int databaseSizeBeforeCreate = dailyRewardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDailyRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDailyRewards() throws Exception {
        // Initialize the database
        dailyRewardRepository.saveAndFlush(dailyReward);

        // Get all the dailyRewardList
        restDailyRewardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyReward.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)))
            .andExpect(jsonPath("$.[*].rewardType").value(hasItem(DEFAULT_REWARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    void getDailyReward() throws Exception {
        // Initialize the database
        dailyRewardRepository.saveAndFlush(dailyReward);

        // Get the dailyReward
        restDailyRewardMockMvc
            .perform(get(ENTITY_API_URL_ID, dailyReward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dailyReward.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME))
            .andExpect(jsonPath("$.rewardType").value(DEFAULT_REWARD_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingDailyReward() throws Exception {
        // Get the dailyReward
        restDailyRewardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDailyReward() throws Exception {
        // Initialize the database
        dailyRewardRepository.saveAndFlush(dailyReward);

        int databaseSizeBeforeUpdate = dailyRewardRepository.findAll().size();

        // Update the dailyReward
        DailyReward updatedDailyReward = dailyRewardRepository.findById(dailyReward.getId()).get();
        // Disconnect from session so that the updates on updatedDailyReward are not directly saved in db
        em.detach(updatedDailyReward);
        updatedDailyReward.time(UPDATED_TIME).rewardType(UPDATED_REWARD_TYPE).amount(UPDATED_AMOUNT);
        DailyRewardDTO dailyRewardDTO = dailyRewardMapper.toDto(updatedDailyReward);

        restDailyRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dailyRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyRewardDTO))
            )
            .andExpect(status().isOk());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeUpdate);
        DailyReward testDailyReward = dailyRewardList.get(dailyRewardList.size() - 1);
        assertThat(testDailyReward.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testDailyReward.getRewardType()).isEqualTo(UPDATED_REWARD_TYPE);
        assertThat(testDailyReward.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingDailyReward() throws Exception {
        int databaseSizeBeforeUpdate = dailyRewardRepository.findAll().size();
        dailyReward.setId(count.incrementAndGet());

        // Create the DailyReward
        DailyRewardDTO dailyRewardDTO = dailyRewardMapper.toDto(dailyReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dailyRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDailyReward() throws Exception {
        int databaseSizeBeforeUpdate = dailyRewardRepository.findAll().size();
        dailyReward.setId(count.incrementAndGet());

        // Create the DailyReward
        DailyRewardDTO dailyRewardDTO = dailyRewardMapper.toDto(dailyReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDailyReward() throws Exception {
        int databaseSizeBeforeUpdate = dailyRewardRepository.findAll().size();
        dailyReward.setId(count.incrementAndGet());

        // Create the DailyReward
        DailyRewardDTO dailyRewardDTO = dailyRewardMapper.toDto(dailyReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyRewardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyRewardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDailyRewardWithPatch() throws Exception {
        // Initialize the database
        dailyRewardRepository.saveAndFlush(dailyReward);

        int databaseSizeBeforeUpdate = dailyRewardRepository.findAll().size();

        // Update the dailyReward using partial update
        DailyReward partialUpdatedDailyReward = new DailyReward();
        partialUpdatedDailyReward.setId(dailyReward.getId());

        partialUpdatedDailyReward.time(UPDATED_TIME).amount(UPDATED_AMOUNT);

        restDailyRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDailyReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDailyReward))
            )
            .andExpect(status().isOk());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeUpdate);
        DailyReward testDailyReward = dailyRewardList.get(dailyRewardList.size() - 1);
        assertThat(testDailyReward.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testDailyReward.getRewardType()).isEqualTo(DEFAULT_REWARD_TYPE);
        assertThat(testDailyReward.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateDailyRewardWithPatch() throws Exception {
        // Initialize the database
        dailyRewardRepository.saveAndFlush(dailyReward);

        int databaseSizeBeforeUpdate = dailyRewardRepository.findAll().size();

        // Update the dailyReward using partial update
        DailyReward partialUpdatedDailyReward = new DailyReward();
        partialUpdatedDailyReward.setId(dailyReward.getId());

        partialUpdatedDailyReward.time(UPDATED_TIME).rewardType(UPDATED_REWARD_TYPE).amount(UPDATED_AMOUNT);

        restDailyRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDailyReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDailyReward))
            )
            .andExpect(status().isOk());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeUpdate);
        DailyReward testDailyReward = dailyRewardList.get(dailyRewardList.size() - 1);
        assertThat(testDailyReward.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testDailyReward.getRewardType()).isEqualTo(UPDATED_REWARD_TYPE);
        assertThat(testDailyReward.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingDailyReward() throws Exception {
        int databaseSizeBeforeUpdate = dailyRewardRepository.findAll().size();
        dailyReward.setId(count.incrementAndGet());

        // Create the DailyReward
        DailyRewardDTO dailyRewardDTO = dailyRewardMapper.toDto(dailyReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dailyRewardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDailyReward() throws Exception {
        int databaseSizeBeforeUpdate = dailyRewardRepository.findAll().size();
        dailyReward.setId(count.incrementAndGet());

        // Create the DailyReward
        DailyRewardDTO dailyRewardDTO = dailyRewardMapper.toDto(dailyReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDailyReward() throws Exception {
        int databaseSizeBeforeUpdate = dailyRewardRepository.findAll().size();
        dailyReward.setId(count.incrementAndGet());

        // Create the DailyReward
        DailyRewardDTO dailyRewardDTO = dailyRewardMapper.toDto(dailyReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyRewardMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dailyRewardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DailyReward in the database
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDailyReward() throws Exception {
        // Initialize the database
        dailyRewardRepository.saveAndFlush(dailyReward);

        int databaseSizeBeforeDelete = dailyRewardRepository.findAll().size();

        // Delete the dailyReward
        restDailyRewardMockMvc
            .perform(delete(ENTITY_API_URL_ID, dailyReward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DailyReward> dailyRewardList = dailyRewardRepository.findAll();
        assertThat(dailyRewardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
