package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.NftReward;
import com.digicade.repository.NftRewardRepository;
import com.digicade.service.dto.NftRewardDTO;
import com.digicade.service.mapper.NftRewardMapper;
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
 * Integration tests for the {@link NftRewardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NftRewardResourceIT {

    private static final Integer DEFAULT_TIX = 1;
    private static final Integer UPDATED_TIX = 2;

    private static final Integer DEFAULT_COMP = 1;
    private static final Integer UPDATED_COMP = 2;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nft-rewards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NftRewardRepository nftRewardRepository;

    @Autowired
    private NftRewardMapper nftRewardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNftRewardMockMvc;

    private NftReward nftReward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NftReward createEntity(EntityManager em) {
        NftReward nftReward = new NftReward().tix(DEFAULT_TIX).comp(DEFAULT_COMP).imageUrl(DEFAULT_IMAGE_URL);
        return nftReward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NftReward createUpdatedEntity(EntityManager em) {
        NftReward nftReward = new NftReward().tix(UPDATED_TIX).comp(UPDATED_COMP).imageUrl(UPDATED_IMAGE_URL);
        return nftReward;
    }

    @BeforeEach
    public void initTest() {
        nftReward = createEntity(em);
    }

    @Test
    @Transactional
    void createNftReward() throws Exception {
        int databaseSizeBeforeCreate = nftRewardRepository.findAll().size();
        // Create the NftReward
        NftRewardDTO nftRewardDTO = nftRewardMapper.toDto(nftReward);
        restNftRewardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nftRewardDTO)))
            .andExpect(status().isCreated());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeCreate + 1);
        NftReward testNftReward = nftRewardList.get(nftRewardList.size() - 1);
        assertThat(testNftReward.getTix()).isEqualTo(DEFAULT_TIX);
        assertThat(testNftReward.getComp()).isEqualTo(DEFAULT_COMP);
        assertThat(testNftReward.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void createNftRewardWithExistingId() throws Exception {
        // Create the NftReward with an existing ID
        nftReward.setId(1L);
        NftRewardDTO nftRewardDTO = nftRewardMapper.toDto(nftReward);

        int databaseSizeBeforeCreate = nftRewardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNftRewardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nftRewardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNftRewards() throws Exception {
        // Initialize the database
        nftRewardRepository.saveAndFlush(nftReward);

        // Get all the nftRewardList
        restNftRewardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nftReward.getId().intValue())))
            .andExpect(jsonPath("$.[*].tix").value(hasItem(DEFAULT_TIX)))
            .andExpect(jsonPath("$.[*].comp").value(hasItem(DEFAULT_COMP)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)));
    }

    @Test
    @Transactional
    void getNftReward() throws Exception {
        // Initialize the database
        nftRewardRepository.saveAndFlush(nftReward);

        // Get the nftReward
        restNftRewardMockMvc
            .perform(get(ENTITY_API_URL_ID, nftReward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nftReward.getId().intValue()))
            .andExpect(jsonPath("$.tix").value(DEFAULT_TIX))
            .andExpect(jsonPath("$.comp").value(DEFAULT_COMP))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL));
    }

    @Test
    @Transactional
    void getNonExistingNftReward() throws Exception {
        // Get the nftReward
        restNftRewardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNftReward() throws Exception {
        // Initialize the database
        nftRewardRepository.saveAndFlush(nftReward);

        int databaseSizeBeforeUpdate = nftRewardRepository.findAll().size();

        // Update the nftReward
        NftReward updatedNftReward = nftRewardRepository.findById(nftReward.getId()).get();
        // Disconnect from session so that the updates on updatedNftReward are not directly saved in db
        em.detach(updatedNftReward);
        updatedNftReward.tix(UPDATED_TIX).comp(UPDATED_COMP).imageUrl(UPDATED_IMAGE_URL);
        NftRewardDTO nftRewardDTO = nftRewardMapper.toDto(updatedNftReward);

        restNftRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nftRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nftRewardDTO))
            )
            .andExpect(status().isOk());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeUpdate);
        NftReward testNftReward = nftRewardList.get(nftRewardList.size() - 1);
        assertThat(testNftReward.getTix()).isEqualTo(UPDATED_TIX);
        assertThat(testNftReward.getComp()).isEqualTo(UPDATED_COMP);
        assertThat(testNftReward.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void putNonExistingNftReward() throws Exception {
        int databaseSizeBeforeUpdate = nftRewardRepository.findAll().size();
        nftReward.setId(count.incrementAndGet());

        // Create the NftReward
        NftRewardDTO nftRewardDTO = nftRewardMapper.toDto(nftReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNftRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nftRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nftRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNftReward() throws Exception {
        int databaseSizeBeforeUpdate = nftRewardRepository.findAll().size();
        nftReward.setId(count.incrementAndGet());

        // Create the NftReward
        NftRewardDTO nftRewardDTO = nftRewardMapper.toDto(nftReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNftRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nftRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNftReward() throws Exception {
        int databaseSizeBeforeUpdate = nftRewardRepository.findAll().size();
        nftReward.setId(count.incrementAndGet());

        // Create the NftReward
        NftRewardDTO nftRewardDTO = nftRewardMapper.toDto(nftReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNftRewardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nftRewardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNftRewardWithPatch() throws Exception {
        // Initialize the database
        nftRewardRepository.saveAndFlush(nftReward);

        int databaseSizeBeforeUpdate = nftRewardRepository.findAll().size();

        // Update the nftReward using partial update
        NftReward partialUpdatedNftReward = new NftReward();
        partialUpdatedNftReward.setId(nftReward.getId());

        restNftRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNftReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNftReward))
            )
            .andExpect(status().isOk());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeUpdate);
        NftReward testNftReward = nftRewardList.get(nftRewardList.size() - 1);
        assertThat(testNftReward.getTix()).isEqualTo(DEFAULT_TIX);
        assertThat(testNftReward.getComp()).isEqualTo(DEFAULT_COMP);
        assertThat(testNftReward.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void fullUpdateNftRewardWithPatch() throws Exception {
        // Initialize the database
        nftRewardRepository.saveAndFlush(nftReward);

        int databaseSizeBeforeUpdate = nftRewardRepository.findAll().size();

        // Update the nftReward using partial update
        NftReward partialUpdatedNftReward = new NftReward();
        partialUpdatedNftReward.setId(nftReward.getId());

        partialUpdatedNftReward.tix(UPDATED_TIX).comp(UPDATED_COMP).imageUrl(UPDATED_IMAGE_URL);

        restNftRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNftReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNftReward))
            )
            .andExpect(status().isOk());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeUpdate);
        NftReward testNftReward = nftRewardList.get(nftRewardList.size() - 1);
        assertThat(testNftReward.getTix()).isEqualTo(UPDATED_TIX);
        assertThat(testNftReward.getComp()).isEqualTo(UPDATED_COMP);
        assertThat(testNftReward.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void patchNonExistingNftReward() throws Exception {
        int databaseSizeBeforeUpdate = nftRewardRepository.findAll().size();
        nftReward.setId(count.incrementAndGet());

        // Create the NftReward
        NftRewardDTO nftRewardDTO = nftRewardMapper.toDto(nftReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNftRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nftRewardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nftRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNftReward() throws Exception {
        int databaseSizeBeforeUpdate = nftRewardRepository.findAll().size();
        nftReward.setId(count.incrementAndGet());

        // Create the NftReward
        NftRewardDTO nftRewardDTO = nftRewardMapper.toDto(nftReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNftRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nftRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNftReward() throws Exception {
        int databaseSizeBeforeUpdate = nftRewardRepository.findAll().size();
        nftReward.setId(count.incrementAndGet());

        // Create the NftReward
        NftRewardDTO nftRewardDTO = nftRewardMapper.toDto(nftReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNftRewardMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nftRewardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NftReward in the database
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNftReward() throws Exception {
        // Initialize the database
        nftRewardRepository.saveAndFlush(nftReward);

        int databaseSizeBeforeDelete = nftRewardRepository.findAll().size();

        // Delete the nftReward
        restNftRewardMockMvc
            .perform(delete(ENTITY_API_URL_ID, nftReward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NftReward> nftRewardList = nftRewardRepository.findAll();
        assertThat(nftRewardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
