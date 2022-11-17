package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.CouponImage;
import com.digicade.repository.CouponImageRepository;
import com.digicade.service.dto.CouponImageDTO;
import com.digicade.service.mapper.CouponImageMapper;
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
 * Integration tests for the {@link CouponImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CouponImageResourceIT {

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/coupon-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CouponImageRepository couponImageRepository;

    @Autowired
    private CouponImageMapper couponImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCouponImageMockMvc;

    private CouponImage couponImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CouponImage createEntity(EntityManager em) {
        CouponImage couponImage = new CouponImage().imageUrl(DEFAULT_IMAGE_URL);
        return couponImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CouponImage createUpdatedEntity(EntityManager em) {
        CouponImage couponImage = new CouponImage().imageUrl(UPDATED_IMAGE_URL);
        return couponImage;
    }

    @BeforeEach
    public void initTest() {
        couponImage = createEntity(em);
    }

    @Test
    @Transactional
    void createCouponImage() throws Exception {
        int databaseSizeBeforeCreate = couponImageRepository.findAll().size();
        // Create the CouponImage
        CouponImageDTO couponImageDTO = couponImageMapper.toDto(couponImage);
        restCouponImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(couponImageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeCreate + 1);
        CouponImage testCouponImage = couponImageList.get(couponImageList.size() - 1);
        assertThat(testCouponImage.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void createCouponImageWithExistingId() throws Exception {
        // Create the CouponImage with an existing ID
        couponImage.setId(1L);
        CouponImageDTO couponImageDTO = couponImageMapper.toDto(couponImage);

        int databaseSizeBeforeCreate = couponImageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCouponImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(couponImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCouponImages() throws Exception {
        // Initialize the database
        couponImageRepository.saveAndFlush(couponImage);

        // Get all the couponImageList
        restCouponImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(couponImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)));
    }

    @Test
    @Transactional
    void getCouponImage() throws Exception {
        // Initialize the database
        couponImageRepository.saveAndFlush(couponImage);

        // Get the couponImage
        restCouponImageMockMvc
            .perform(get(ENTITY_API_URL_ID, couponImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(couponImage.getId().intValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL));
    }

    @Test
    @Transactional
    void getNonExistingCouponImage() throws Exception {
        // Get the couponImage
        restCouponImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCouponImage() throws Exception {
        // Initialize the database
        couponImageRepository.saveAndFlush(couponImage);

        int databaseSizeBeforeUpdate = couponImageRepository.findAll().size();

        // Update the couponImage
        CouponImage updatedCouponImage = couponImageRepository.findById(couponImage.getId()).get();
        // Disconnect from session so that the updates on updatedCouponImage are not directly saved in db
        em.detach(updatedCouponImage);
        updatedCouponImage.imageUrl(UPDATED_IMAGE_URL);
        CouponImageDTO couponImageDTO = couponImageMapper.toDto(updatedCouponImage);

        restCouponImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couponImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(couponImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeUpdate);
        CouponImage testCouponImage = couponImageList.get(couponImageList.size() - 1);
        assertThat(testCouponImage.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void putNonExistingCouponImage() throws Exception {
        int databaseSizeBeforeUpdate = couponImageRepository.findAll().size();
        couponImage.setId(count.incrementAndGet());

        // Create the CouponImage
        CouponImageDTO couponImageDTO = couponImageMapper.toDto(couponImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couponImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(couponImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCouponImage() throws Exception {
        int databaseSizeBeforeUpdate = couponImageRepository.findAll().size();
        couponImage.setId(count.incrementAndGet());

        // Create the CouponImage
        CouponImageDTO couponImageDTO = couponImageMapper.toDto(couponImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(couponImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCouponImage() throws Exception {
        int databaseSizeBeforeUpdate = couponImageRepository.findAll().size();
        couponImage.setId(count.incrementAndGet());

        // Create the CouponImage
        CouponImageDTO couponImageDTO = couponImageMapper.toDto(couponImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(couponImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCouponImageWithPatch() throws Exception {
        // Initialize the database
        couponImageRepository.saveAndFlush(couponImage);

        int databaseSizeBeforeUpdate = couponImageRepository.findAll().size();

        // Update the couponImage using partial update
        CouponImage partialUpdatedCouponImage = new CouponImage();
        partialUpdatedCouponImage.setId(couponImage.getId());

        partialUpdatedCouponImage.imageUrl(UPDATED_IMAGE_URL);

        restCouponImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCouponImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCouponImage))
            )
            .andExpect(status().isOk());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeUpdate);
        CouponImage testCouponImage = couponImageList.get(couponImageList.size() - 1);
        assertThat(testCouponImage.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void fullUpdateCouponImageWithPatch() throws Exception {
        // Initialize the database
        couponImageRepository.saveAndFlush(couponImage);

        int databaseSizeBeforeUpdate = couponImageRepository.findAll().size();

        // Update the couponImage using partial update
        CouponImage partialUpdatedCouponImage = new CouponImage();
        partialUpdatedCouponImage.setId(couponImage.getId());

        partialUpdatedCouponImage.imageUrl(UPDATED_IMAGE_URL);

        restCouponImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCouponImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCouponImage))
            )
            .andExpect(status().isOk());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeUpdate);
        CouponImage testCouponImage = couponImageList.get(couponImageList.size() - 1);
        assertThat(testCouponImage.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void patchNonExistingCouponImage() throws Exception {
        int databaseSizeBeforeUpdate = couponImageRepository.findAll().size();
        couponImage.setId(count.incrementAndGet());

        // Create the CouponImage
        CouponImageDTO couponImageDTO = couponImageMapper.toDto(couponImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, couponImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(couponImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCouponImage() throws Exception {
        int databaseSizeBeforeUpdate = couponImageRepository.findAll().size();
        couponImage.setId(count.incrementAndGet());

        // Create the CouponImage
        CouponImageDTO couponImageDTO = couponImageMapper.toDto(couponImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(couponImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCouponImage() throws Exception {
        int databaseSizeBeforeUpdate = couponImageRepository.findAll().size();
        couponImage.setId(count.incrementAndGet());

        // Create the CouponImage
        CouponImageDTO couponImageDTO = couponImageMapper.toDto(couponImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponImageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(couponImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CouponImage in the database
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCouponImage() throws Exception {
        // Initialize the database
        couponImageRepository.saveAndFlush(couponImage);

        int databaseSizeBeforeDelete = couponImageRepository.findAll().size();

        // Delete the couponImage
        restCouponImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, couponImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CouponImage> couponImageList = couponImageRepository.findAll();
        assertThat(couponImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
