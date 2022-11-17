package com.digicade.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digicade.IntegrationTest;
import com.digicade.domain.DigiUser;
import com.digicade.repository.DigiUserRepository;
import com.digicade.service.dto.DigiUserDTO;
import com.digicade.service.mapper.DigiUserMapper;
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
 * Integration tests for the {@link DigiUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DigiUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String DEFAULT_PROMO_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROMO_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/digi-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DigiUserRepository digiUserRepository;

    @Autowired
    private DigiUserMapper digiUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDigiUserMockMvc;

    private DigiUser digiUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DigiUser createEntity(EntityManager em) {
        DigiUser digiUser = new DigiUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .userName(DEFAULT_USER_NAME)
            .email(DEFAULT_EMAIL)
            .dob(DEFAULT_DOB)
            .age(DEFAULT_AGE)
            .promoCode(DEFAULT_PROMO_CODE);
        return digiUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DigiUser createUpdatedEntity(EntityManager em) {
        DigiUser digiUser = new DigiUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .userName(UPDATED_USER_NAME)
            .email(UPDATED_EMAIL)
            .dob(UPDATED_DOB)
            .age(UPDATED_AGE)
            .promoCode(UPDATED_PROMO_CODE);
        return digiUser;
    }

    @BeforeEach
    public void initTest() {
        digiUser = createEntity(em);
    }

    @Test
    @Transactional
    void createDigiUser() throws Exception {
        int databaseSizeBeforeCreate = digiUserRepository.findAll().size();
        // Create the DigiUser
        DigiUserDTO digiUserDTO = digiUserMapper.toDto(digiUser);
        restDigiUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(digiUserDTO)))
            .andExpect(status().isCreated());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeCreate + 1);
        DigiUser testDigiUser = digiUserList.get(digiUserList.size() - 1);
        assertThat(testDigiUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testDigiUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testDigiUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testDigiUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDigiUser.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testDigiUser.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testDigiUser.getPromoCode()).isEqualTo(DEFAULT_PROMO_CODE);
    }

    @Test
    @Transactional
    void createDigiUserWithExistingId() throws Exception {
        // Create the DigiUser with an existing ID
        digiUser.setId(1L);
        DigiUserDTO digiUserDTO = digiUserMapper.toDto(digiUser);

        int databaseSizeBeforeCreate = digiUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDigiUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(digiUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDigiUsers() throws Exception {
        // Initialize the database
        digiUserRepository.saveAndFlush(digiUser);

        // Get all the digiUserList
        restDigiUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(digiUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].promoCode").value(hasItem(DEFAULT_PROMO_CODE)));
    }

    @Test
    @Transactional
    void getDigiUser() throws Exception {
        // Initialize the database
        digiUserRepository.saveAndFlush(digiUser);

        // Get the digiUser
        restDigiUserMockMvc
            .perform(get(ENTITY_API_URL_ID, digiUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(digiUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.promoCode").value(DEFAULT_PROMO_CODE));
    }

    @Test
    @Transactional
    void getNonExistingDigiUser() throws Exception {
        // Get the digiUser
        restDigiUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDigiUser() throws Exception {
        // Initialize the database
        digiUserRepository.saveAndFlush(digiUser);

        int databaseSizeBeforeUpdate = digiUserRepository.findAll().size();

        // Update the digiUser
        DigiUser updatedDigiUser = digiUserRepository.findById(digiUser.getId()).get();
        // Disconnect from session so that the updates on updatedDigiUser are not directly saved in db
        em.detach(updatedDigiUser);
        updatedDigiUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .userName(UPDATED_USER_NAME)
            .email(UPDATED_EMAIL)
            .dob(UPDATED_DOB)
            .age(UPDATED_AGE)
            .promoCode(UPDATED_PROMO_CODE);
        DigiUserDTO digiUserDTO = digiUserMapper.toDto(updatedDigiUser);

        restDigiUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, digiUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(digiUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeUpdate);
        DigiUser testDigiUser = digiUserList.get(digiUserList.size() - 1);
        assertThat(testDigiUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDigiUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDigiUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testDigiUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDigiUser.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testDigiUser.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testDigiUser.getPromoCode()).isEqualTo(UPDATED_PROMO_CODE);
    }

    @Test
    @Transactional
    void putNonExistingDigiUser() throws Exception {
        int databaseSizeBeforeUpdate = digiUserRepository.findAll().size();
        digiUser.setId(count.incrementAndGet());

        // Create the DigiUser
        DigiUserDTO digiUserDTO = digiUserMapper.toDto(digiUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDigiUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, digiUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(digiUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDigiUser() throws Exception {
        int databaseSizeBeforeUpdate = digiUserRepository.findAll().size();
        digiUser.setId(count.incrementAndGet());

        // Create the DigiUser
        DigiUserDTO digiUserDTO = digiUserMapper.toDto(digiUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDigiUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(digiUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDigiUser() throws Exception {
        int databaseSizeBeforeUpdate = digiUserRepository.findAll().size();
        digiUser.setId(count.incrementAndGet());

        // Create the DigiUser
        DigiUserDTO digiUserDTO = digiUserMapper.toDto(digiUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDigiUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(digiUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDigiUserWithPatch() throws Exception {
        // Initialize the database
        digiUserRepository.saveAndFlush(digiUser);

        int databaseSizeBeforeUpdate = digiUserRepository.findAll().size();

        // Update the digiUser using partial update
        DigiUser partialUpdatedDigiUser = new DigiUser();
        partialUpdatedDigiUser.setId(digiUser.getId());

        partialUpdatedDigiUser.lastName(UPDATED_LAST_NAME).age(UPDATED_AGE);

        restDigiUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDigiUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDigiUser))
            )
            .andExpect(status().isOk());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeUpdate);
        DigiUser testDigiUser = digiUserList.get(digiUserList.size() - 1);
        assertThat(testDigiUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testDigiUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDigiUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testDigiUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDigiUser.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testDigiUser.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testDigiUser.getPromoCode()).isEqualTo(DEFAULT_PROMO_CODE);
    }

    @Test
    @Transactional
    void fullUpdateDigiUserWithPatch() throws Exception {
        // Initialize the database
        digiUserRepository.saveAndFlush(digiUser);

        int databaseSizeBeforeUpdate = digiUserRepository.findAll().size();

        // Update the digiUser using partial update
        DigiUser partialUpdatedDigiUser = new DigiUser();
        partialUpdatedDigiUser.setId(digiUser.getId());

        partialUpdatedDigiUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .userName(UPDATED_USER_NAME)
            .email(UPDATED_EMAIL)
            .dob(UPDATED_DOB)
            .age(UPDATED_AGE)
            .promoCode(UPDATED_PROMO_CODE);

        restDigiUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDigiUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDigiUser))
            )
            .andExpect(status().isOk());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeUpdate);
        DigiUser testDigiUser = digiUserList.get(digiUserList.size() - 1);
        assertThat(testDigiUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDigiUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDigiUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testDigiUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDigiUser.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testDigiUser.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testDigiUser.getPromoCode()).isEqualTo(UPDATED_PROMO_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingDigiUser() throws Exception {
        int databaseSizeBeforeUpdate = digiUserRepository.findAll().size();
        digiUser.setId(count.incrementAndGet());

        // Create the DigiUser
        DigiUserDTO digiUserDTO = digiUserMapper.toDto(digiUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDigiUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, digiUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(digiUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDigiUser() throws Exception {
        int databaseSizeBeforeUpdate = digiUserRepository.findAll().size();
        digiUser.setId(count.incrementAndGet());

        // Create the DigiUser
        DigiUserDTO digiUserDTO = digiUserMapper.toDto(digiUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDigiUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(digiUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDigiUser() throws Exception {
        int databaseSizeBeforeUpdate = digiUserRepository.findAll().size();
        digiUser.setId(count.incrementAndGet());

        // Create the DigiUser
        DigiUserDTO digiUserDTO = digiUserMapper.toDto(digiUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDigiUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(digiUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DigiUser in the database
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDigiUser() throws Exception {
        // Initialize the database
        digiUserRepository.saveAndFlush(digiUser);

        int databaseSizeBeforeDelete = digiUserRepository.findAll().size();

        // Delete the digiUser
        restDigiUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, digiUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DigiUser> digiUserList = digiUserRepository.findAll();
        assertThat(digiUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
