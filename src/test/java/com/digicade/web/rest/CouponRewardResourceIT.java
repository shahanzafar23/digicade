package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.CouponReward;
import com.digicade.repository.CouponRewardRepository;
import com.digicade.service.dto.CouponRewardDTO;
import com.digicade.service.mapper.CouponRewardMapper;
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
 * Integration tests for the {@link CouponRewardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CouponRewardResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIX = 1;
    private static final Integer UPDATED_TIX = 2;

    private static final Integer DEFAULT_COMP = 1;
    private static final Integer UPDATED_COMP = 2;

    private static final LocalDate DEFAULT_EXPIRY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/coupon-rewards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CouponRewardRepository couponRewardRepository;

    @Autowired
    private CouponRewardMapper couponRewardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCouponRewardMockMvc;

    private CouponReward couponReward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CouponReward createEntity(EntityManager em) {
        CouponReward couponReward = new CouponReward()
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION)
            .tix(DEFAULT_TIX)
            .comp(DEFAULT_COMP)
            .expiry(DEFAULT_EXPIRY);
        return couponReward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CouponReward createUpdatedEntity(EntityManager em) {
        CouponReward couponReward = new CouponReward()
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .tix(UPDATED_TIX)
            .comp(UPDATED_COMP)
            .expiry(UPDATED_EXPIRY);
        return couponReward;
    }

    @BeforeEach
    public void initTest() {
        couponReward = createEntity(em);
    }

    @Test
    @Transactional
    void createCouponReward() throws Exception {
        int databaseSizeBeforeCreate = couponRewardRepository.findAll().size();
        // Create the CouponReward
        CouponRewardDTO couponRewardDTO = couponRewardMapper.toDto(couponReward);
        restCouponRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(couponRewardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeCreate + 1);
        CouponReward testCouponReward = couponRewardList.get(couponRewardList.size() - 1);
        assertThat(testCouponReward.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCouponReward.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCouponReward.getTix()).isEqualTo(DEFAULT_TIX);
        assertThat(testCouponReward.getComp()).isEqualTo(DEFAULT_COMP);
        assertThat(testCouponReward.getExpiry()).isEqualTo(DEFAULT_EXPIRY);
    }

    @Test
    @Transactional
    void createCouponRewardWithExistingId() throws Exception {
        // Create the CouponReward with an existing ID
        couponReward.setId(1L);
        CouponRewardDTO couponRewardDTO = couponRewardMapper.toDto(couponReward);

        int databaseSizeBeforeCreate = couponRewardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCouponRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(couponRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCouponRewards() throws Exception {
        // Initialize the database
        couponRewardRepository.saveAndFlush(couponReward);

        // Get all the couponRewardList
        restCouponRewardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(couponReward.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].tix").value(hasItem(DEFAULT_TIX)))
            .andExpect(jsonPath("$.[*].comp").value(hasItem(DEFAULT_COMP)))
            .andExpect(jsonPath("$.[*].expiry").value(hasItem(DEFAULT_EXPIRY.toString())));
    }

    @Test
    @Transactional
    void getCouponReward() throws Exception {
        // Initialize the database
        couponRewardRepository.saveAndFlush(couponReward);

        // Get the couponReward
        restCouponRewardMockMvc
            .perform(get(ENTITY_API_URL_ID, couponReward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(couponReward.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.tix").value(DEFAULT_TIX))
            .andExpect(jsonPath("$.comp").value(DEFAULT_COMP))
            .andExpect(jsonPath("$.expiry").value(DEFAULT_EXPIRY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCouponReward() throws Exception {
        // Get the couponReward
        restCouponRewardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCouponReward() throws Exception {
        // Initialize the database
        couponRewardRepository.saveAndFlush(couponReward);

        int databaseSizeBeforeUpdate = couponRewardRepository.findAll().size();

        // Update the couponReward
        CouponReward updatedCouponReward = couponRewardRepository.findById(couponReward.getId()).get();
        // Disconnect from session so that the updates on updatedCouponReward are not directly saved in db
        em.detach(updatedCouponReward);
        updatedCouponReward
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .tix(UPDATED_TIX)
            .comp(UPDATED_COMP)
            .expiry(UPDATED_EXPIRY);
        CouponRewardDTO couponRewardDTO = couponRewardMapper.toDto(updatedCouponReward);

        restCouponRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couponRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(couponRewardDTO))
            )
            .andExpect(status().isOk());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeUpdate);
        CouponReward testCouponReward = couponRewardList.get(couponRewardList.size() - 1);
        assertThat(testCouponReward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCouponReward.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCouponReward.getTix()).isEqualTo(UPDATED_TIX);
        assertThat(testCouponReward.getComp()).isEqualTo(UPDATED_COMP);
        assertThat(testCouponReward.getExpiry()).isEqualTo(UPDATED_EXPIRY);
    }

    @Test
    @Transactional
    void putNonExistingCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = couponRewardRepository.findAll().size();
        couponReward.setId(count.incrementAndGet());

        // Create the CouponReward
        CouponRewardDTO couponRewardDTO = couponRewardMapper.toDto(couponReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couponRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(couponRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = couponRewardRepository.findAll().size();
        couponReward.setId(count.incrementAndGet());

        // Create the CouponReward
        CouponRewardDTO couponRewardDTO = couponRewardMapper.toDto(couponReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(couponRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = couponRewardRepository.findAll().size();
        couponReward.setId(count.incrementAndGet());

        // Create the CouponReward
        CouponRewardDTO couponRewardDTO = couponRewardMapper.toDto(couponReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponRewardMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(couponRewardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCouponRewardWithPatch() throws Exception {
        // Initialize the database
        couponRewardRepository.saveAndFlush(couponReward);

        int databaseSizeBeforeUpdate = couponRewardRepository.findAll().size();

        // Update the couponReward using partial update
        CouponReward partialUpdatedCouponReward = new CouponReward();
        partialUpdatedCouponReward.setId(couponReward.getId());

        partialUpdatedCouponReward.tix(UPDATED_TIX).comp(UPDATED_COMP);

        restCouponRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCouponReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCouponReward))
            )
            .andExpect(status().isOk());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeUpdate);
        CouponReward testCouponReward = couponRewardList.get(couponRewardList.size() - 1);
        assertThat(testCouponReward.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCouponReward.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCouponReward.getTix()).isEqualTo(UPDATED_TIX);
        assertThat(testCouponReward.getComp()).isEqualTo(UPDATED_COMP);
        assertThat(testCouponReward.getExpiry()).isEqualTo(DEFAULT_EXPIRY);
    }

    @Test
    @Transactional
    void fullUpdateCouponRewardWithPatch() throws Exception {
        // Initialize the database
        couponRewardRepository.saveAndFlush(couponReward);

        int databaseSizeBeforeUpdate = couponRewardRepository.findAll().size();

        // Update the couponReward using partial update
        CouponReward partialUpdatedCouponReward = new CouponReward();
        partialUpdatedCouponReward.setId(couponReward.getId());

        partialUpdatedCouponReward
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .tix(UPDATED_TIX)
            .comp(UPDATED_COMP)
            .expiry(UPDATED_EXPIRY);

        restCouponRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCouponReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCouponReward))
            )
            .andExpect(status().isOk());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeUpdate);
        CouponReward testCouponReward = couponRewardList.get(couponRewardList.size() - 1);
        assertThat(testCouponReward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCouponReward.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCouponReward.getTix()).isEqualTo(UPDATED_TIX);
        assertThat(testCouponReward.getComp()).isEqualTo(UPDATED_COMP);
        assertThat(testCouponReward.getExpiry()).isEqualTo(UPDATED_EXPIRY);
    }

    @Test
    @Transactional
    void patchNonExistingCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = couponRewardRepository.findAll().size();
        couponReward.setId(count.incrementAndGet());

        // Create the CouponReward
        CouponRewardDTO couponRewardDTO = couponRewardMapper.toDto(couponReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, couponRewardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(couponRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = couponRewardRepository.findAll().size();
        couponReward.setId(count.incrementAndGet());

        // Create the CouponReward
        CouponRewardDTO couponRewardDTO = couponRewardMapper.toDto(couponReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(couponRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCouponReward() throws Exception {
        int databaseSizeBeforeUpdate = couponRewardRepository.findAll().size();
        couponReward.setId(count.incrementAndGet());

        // Create the CouponReward
        CouponRewardDTO couponRewardDTO = couponRewardMapper.toDto(couponReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponRewardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(couponRewardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CouponReward in the database
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCouponReward() throws Exception {
        // Initialize the database
        couponRewardRepository.saveAndFlush(couponReward);

        int databaseSizeBeforeDelete = couponRewardRepository.findAll().size();

        // Delete the couponReward
        restCouponRewardMockMvc
            .perform(delete(ENTITY_API_URL_ID, couponReward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CouponReward> couponRewardList = couponRewardRepository.findAll();
        assertThat(couponRewardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
