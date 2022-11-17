package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.CoinPackage;
import com.digicade.repository.CoinPackageRepository;
import com.digicade.service.dto.CoinPackageDTO;
import com.digicade.service.mapper.CoinPackageMapper;
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
 * Integration tests for the {@link CoinPackageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoinPackageResourceIT {

    private static final Integer DEFAULT_COINS = 1;
    private static final Integer UPDATED_COINS = 2;

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;

    private static final String ENTITY_API_URL = "/api/coin-packages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoinPackageRepository coinPackageRepository;

    @Autowired
    private CoinPackageMapper coinPackageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoinPackageMockMvc;

    private CoinPackage coinPackage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoinPackage createEntity(EntityManager em) {
        CoinPackage coinPackage = new CoinPackage().coins(DEFAULT_COINS).cost(DEFAULT_COST);
        return coinPackage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoinPackage createUpdatedEntity(EntityManager em) {
        CoinPackage coinPackage = new CoinPackage().coins(UPDATED_COINS).cost(UPDATED_COST);
        return coinPackage;
    }

    @BeforeEach
    public void initTest() {
        coinPackage = createEntity(em);
    }

    @Test
    @Transactional
    void createCoinPackage() throws Exception {
        int databaseSizeBeforeCreate = coinPackageRepository.findAll().size();
        // Create the CoinPackage
        CoinPackageDTO coinPackageDTO = coinPackageMapper.toDto(coinPackage);
        restCoinPackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coinPackageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeCreate + 1);
        CoinPackage testCoinPackage = coinPackageList.get(coinPackageList.size() - 1);
        assertThat(testCoinPackage.getCoins()).isEqualTo(DEFAULT_COINS);
        assertThat(testCoinPackage.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void createCoinPackageWithExistingId() throws Exception {
        // Create the CoinPackage with an existing ID
        coinPackage.setId(1L);
        CoinPackageDTO coinPackageDTO = coinPackageMapper.toDto(coinPackage);

        int databaseSizeBeforeCreate = coinPackageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoinPackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coinPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCoinPackages() throws Exception {
        // Initialize the database
        coinPackageRepository.saveAndFlush(coinPackage);

        // Get all the coinPackageList
        restCoinPackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coinPackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].coins").value(hasItem(DEFAULT_COINS)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())));
    }

    @Test
    @Transactional
    void getCoinPackage() throws Exception {
        // Initialize the database
        coinPackageRepository.saveAndFlush(coinPackage);

        // Get the coinPackage
        restCoinPackageMockMvc
            .perform(get(ENTITY_API_URL_ID, coinPackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coinPackage.getId().intValue()))
            .andExpect(jsonPath("$.coins").value(DEFAULT_COINS))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCoinPackage() throws Exception {
        // Get the coinPackage
        restCoinPackageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoinPackage() throws Exception {
        // Initialize the database
        coinPackageRepository.saveAndFlush(coinPackage);

        int databaseSizeBeforeUpdate = coinPackageRepository.findAll().size();

        // Update the coinPackage
        CoinPackage updatedCoinPackage = coinPackageRepository.findById(coinPackage.getId()).get();
        // Disconnect from session so that the updates on updatedCoinPackage are not directly saved in db
        em.detach(updatedCoinPackage);
        updatedCoinPackage.coins(UPDATED_COINS).cost(UPDATED_COST);
        CoinPackageDTO coinPackageDTO = coinPackageMapper.toDto(updatedCoinPackage);

        restCoinPackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coinPackageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coinPackageDTO))
            )
            .andExpect(status().isOk());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeUpdate);
        CoinPackage testCoinPackage = coinPackageList.get(coinPackageList.size() - 1);
        assertThat(testCoinPackage.getCoins()).isEqualTo(UPDATED_COINS);
        assertThat(testCoinPackage.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void putNonExistingCoinPackage() throws Exception {
        int databaseSizeBeforeUpdate = coinPackageRepository.findAll().size();
        coinPackage.setId(count.incrementAndGet());

        // Create the CoinPackage
        CoinPackageDTO coinPackageDTO = coinPackageMapper.toDto(coinPackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoinPackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coinPackageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coinPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoinPackage() throws Exception {
        int databaseSizeBeforeUpdate = coinPackageRepository.findAll().size();
        coinPackage.setId(count.incrementAndGet());

        // Create the CoinPackage
        CoinPackageDTO coinPackageDTO = coinPackageMapper.toDto(coinPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoinPackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coinPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoinPackage() throws Exception {
        int databaseSizeBeforeUpdate = coinPackageRepository.findAll().size();
        coinPackage.setId(count.incrementAndGet());

        // Create the CoinPackage
        CoinPackageDTO coinPackageDTO = coinPackageMapper.toDto(coinPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoinPackageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coinPackageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoinPackageWithPatch() throws Exception {
        // Initialize the database
        coinPackageRepository.saveAndFlush(coinPackage);

        int databaseSizeBeforeUpdate = coinPackageRepository.findAll().size();

        // Update the coinPackage using partial update
        CoinPackage partialUpdatedCoinPackage = new CoinPackage();
        partialUpdatedCoinPackage.setId(coinPackage.getId());

        partialUpdatedCoinPackage.coins(UPDATED_COINS).cost(UPDATED_COST);

        restCoinPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoinPackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoinPackage))
            )
            .andExpect(status().isOk());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeUpdate);
        CoinPackage testCoinPackage = coinPackageList.get(coinPackageList.size() - 1);
        assertThat(testCoinPackage.getCoins()).isEqualTo(UPDATED_COINS);
        assertThat(testCoinPackage.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void fullUpdateCoinPackageWithPatch() throws Exception {
        // Initialize the database
        coinPackageRepository.saveAndFlush(coinPackage);

        int databaseSizeBeforeUpdate = coinPackageRepository.findAll().size();

        // Update the coinPackage using partial update
        CoinPackage partialUpdatedCoinPackage = new CoinPackage();
        partialUpdatedCoinPackage.setId(coinPackage.getId());

        partialUpdatedCoinPackage.coins(UPDATED_COINS).cost(UPDATED_COST);

        restCoinPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoinPackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoinPackage))
            )
            .andExpect(status().isOk());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeUpdate);
        CoinPackage testCoinPackage = coinPackageList.get(coinPackageList.size() - 1);
        assertThat(testCoinPackage.getCoins()).isEqualTo(UPDATED_COINS);
        assertThat(testCoinPackage.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void patchNonExistingCoinPackage() throws Exception {
        int databaseSizeBeforeUpdate = coinPackageRepository.findAll().size();
        coinPackage.setId(count.incrementAndGet());

        // Create the CoinPackage
        CoinPackageDTO coinPackageDTO = coinPackageMapper.toDto(coinPackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoinPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coinPackageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coinPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoinPackage() throws Exception {
        int databaseSizeBeforeUpdate = coinPackageRepository.findAll().size();
        coinPackage.setId(count.incrementAndGet());

        // Create the CoinPackage
        CoinPackageDTO coinPackageDTO = coinPackageMapper.toDto(coinPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoinPackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coinPackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoinPackage() throws Exception {
        int databaseSizeBeforeUpdate = coinPackageRepository.findAll().size();
        coinPackage.setId(count.incrementAndGet());

        // Create the CoinPackage
        CoinPackageDTO coinPackageDTO = coinPackageMapper.toDto(coinPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoinPackageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(coinPackageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoinPackage in the database
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoinPackage() throws Exception {
        // Initialize the database
        coinPackageRepository.saveAndFlush(coinPackage);

        int databaseSizeBeforeDelete = coinPackageRepository.findAll().size();

        // Delete the coinPackage
        restCoinPackageMockMvc
            .perform(delete(ENTITY_API_URL_ID, coinPackage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CoinPackage> coinPackageList = coinPackageRepository.findAll();
        assertThat(coinPackageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
