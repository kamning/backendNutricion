package com.grupo5.nutricion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.grupo5.nutricion.IntegrationTest;
import com.grupo5.nutricion.domain.KitchenRecipes;
import com.grupo5.nutricion.repository.KitchenRecipesRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link KitchenRecipesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KitchenRecipesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INGREDIENTS = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENTS = "BBBBBBBBBB";

    private static final String DEFAULT_PREPARATION = "AAAAAAAAAA";
    private static final String UPDATED_PREPARATION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/kitchen-recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KitchenRecipesRepository kitchenRecipesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKitchenRecipesMockMvc;

    private KitchenRecipes kitchenRecipes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KitchenRecipes createEntity(EntityManager em) {
        KitchenRecipes kitchenRecipes = new KitchenRecipes()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .ingredients(DEFAULT_INGREDIENTS)
            .preparation(DEFAULT_PREPARATION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .status(DEFAULT_STATUS);
        return kitchenRecipes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KitchenRecipes createUpdatedEntity(EntityManager em) {
        KitchenRecipes kitchenRecipes = new KitchenRecipes()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .ingredients(UPDATED_INGREDIENTS)
            .preparation(UPDATED_PREPARATION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .status(UPDATED_STATUS);
        return kitchenRecipes;
    }

    @BeforeEach
    public void initTest() {
        kitchenRecipes = createEntity(em);
    }

    @Test
    @Transactional
    void createKitchenRecipes() throws Exception {
        int databaseSizeBeforeCreate = kitchenRecipesRepository.findAll().size();
        // Create the KitchenRecipes
        restKitchenRecipesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kitchenRecipes))
            )
            .andExpect(status().isCreated());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeCreate + 1);
        KitchenRecipes testKitchenRecipes = kitchenRecipesList.get(kitchenRecipesList.size() - 1);
        assertThat(testKitchenRecipes.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testKitchenRecipes.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testKitchenRecipes.getIngredients()).isEqualTo(DEFAULT_INGREDIENTS);
        assertThat(testKitchenRecipes.getPreparation()).isEqualTo(DEFAULT_PREPARATION);
        assertThat(testKitchenRecipes.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testKitchenRecipes.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testKitchenRecipes.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createKitchenRecipesWithExistingId() throws Exception {
        // Create the KitchenRecipes with an existing ID
        kitchenRecipes.setId(1L);

        int databaseSizeBeforeCreate = kitchenRecipesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKitchenRecipesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kitchenRecipes))
            )
            .andExpect(status().isBadRequest());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = kitchenRecipesRepository.findAll().size();
        // set the field null
        kitchenRecipes.setName(null);

        // Create the KitchenRecipes, which fails.

        restKitchenRecipesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kitchenRecipes))
            )
            .andExpect(status().isBadRequest());

        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKitchenRecipes() throws Exception {
        // Initialize the database
        kitchenRecipesRepository.saveAndFlush(kitchenRecipes);

        // Get all the kitchenRecipesList
        restKitchenRecipesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kitchenRecipes.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS.toString())))
            .andExpect(jsonPath("$.[*].preparation").value(hasItem(DEFAULT_PREPARATION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getKitchenRecipes() throws Exception {
        // Initialize the database
        kitchenRecipesRepository.saveAndFlush(kitchenRecipes);

        // Get the kitchenRecipes
        restKitchenRecipesMockMvc
            .perform(get(ENTITY_API_URL_ID, kitchenRecipes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kitchenRecipes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.ingredients").value(DEFAULT_INGREDIENTS.toString()))
            .andExpect(jsonPath("$.preparation").value(DEFAULT_PREPARATION.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingKitchenRecipes() throws Exception {
        // Get the kitchenRecipes
        restKitchenRecipesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKitchenRecipes() throws Exception {
        // Initialize the database
        kitchenRecipesRepository.saveAndFlush(kitchenRecipes);

        int databaseSizeBeforeUpdate = kitchenRecipesRepository.findAll().size();

        // Update the kitchenRecipes
        KitchenRecipes updatedKitchenRecipes = kitchenRecipesRepository.findById(kitchenRecipes.getId()).get();
        // Disconnect from session so that the updates on updatedKitchenRecipes are not directly saved in db
        em.detach(updatedKitchenRecipes);
        updatedKitchenRecipes
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .ingredients(UPDATED_INGREDIENTS)
            .preparation(UPDATED_PREPARATION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restKitchenRecipesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKitchenRecipes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedKitchenRecipes))
            )
            .andExpect(status().isOk());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeUpdate);
        KitchenRecipes testKitchenRecipes = kitchenRecipesList.get(kitchenRecipesList.size() - 1);
        assertThat(testKitchenRecipes.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testKitchenRecipes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testKitchenRecipes.getIngredients()).isEqualTo(UPDATED_INGREDIENTS);
        assertThat(testKitchenRecipes.getPreparation()).isEqualTo(UPDATED_PREPARATION);
        assertThat(testKitchenRecipes.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testKitchenRecipes.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testKitchenRecipes.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingKitchenRecipes() throws Exception {
        int databaseSizeBeforeUpdate = kitchenRecipesRepository.findAll().size();
        kitchenRecipes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKitchenRecipesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kitchenRecipes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kitchenRecipes))
            )
            .andExpect(status().isBadRequest());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKitchenRecipes() throws Exception {
        int databaseSizeBeforeUpdate = kitchenRecipesRepository.findAll().size();
        kitchenRecipes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKitchenRecipesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kitchenRecipes))
            )
            .andExpect(status().isBadRequest());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKitchenRecipes() throws Exception {
        int databaseSizeBeforeUpdate = kitchenRecipesRepository.findAll().size();
        kitchenRecipes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKitchenRecipesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kitchenRecipes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKitchenRecipesWithPatch() throws Exception {
        // Initialize the database
        kitchenRecipesRepository.saveAndFlush(kitchenRecipes);

        int databaseSizeBeforeUpdate = kitchenRecipesRepository.findAll().size();

        // Update the kitchenRecipes using partial update
        KitchenRecipes partialUpdatedKitchenRecipes = new KitchenRecipes();
        partialUpdatedKitchenRecipes.setId(kitchenRecipes.getId());

        partialUpdatedKitchenRecipes.description(UPDATED_DESCRIPTION).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restKitchenRecipesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKitchenRecipes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKitchenRecipes))
            )
            .andExpect(status().isOk());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeUpdate);
        KitchenRecipes testKitchenRecipes = kitchenRecipesList.get(kitchenRecipesList.size() - 1);
        assertThat(testKitchenRecipes.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testKitchenRecipes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testKitchenRecipes.getIngredients()).isEqualTo(DEFAULT_INGREDIENTS);
        assertThat(testKitchenRecipes.getPreparation()).isEqualTo(DEFAULT_PREPARATION);
        assertThat(testKitchenRecipes.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testKitchenRecipes.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testKitchenRecipes.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateKitchenRecipesWithPatch() throws Exception {
        // Initialize the database
        kitchenRecipesRepository.saveAndFlush(kitchenRecipes);

        int databaseSizeBeforeUpdate = kitchenRecipesRepository.findAll().size();

        // Update the kitchenRecipes using partial update
        KitchenRecipes partialUpdatedKitchenRecipes = new KitchenRecipes();
        partialUpdatedKitchenRecipes.setId(kitchenRecipes.getId());

        partialUpdatedKitchenRecipes
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .ingredients(UPDATED_INGREDIENTS)
            .preparation(UPDATED_PREPARATION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restKitchenRecipesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKitchenRecipes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKitchenRecipes))
            )
            .andExpect(status().isOk());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeUpdate);
        KitchenRecipes testKitchenRecipes = kitchenRecipesList.get(kitchenRecipesList.size() - 1);
        assertThat(testKitchenRecipes.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testKitchenRecipes.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testKitchenRecipes.getIngredients()).isEqualTo(UPDATED_INGREDIENTS);
        assertThat(testKitchenRecipes.getPreparation()).isEqualTo(UPDATED_PREPARATION);
        assertThat(testKitchenRecipes.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testKitchenRecipes.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testKitchenRecipes.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingKitchenRecipes() throws Exception {
        int databaseSizeBeforeUpdate = kitchenRecipesRepository.findAll().size();
        kitchenRecipes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKitchenRecipesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kitchenRecipes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kitchenRecipes))
            )
            .andExpect(status().isBadRequest());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKitchenRecipes() throws Exception {
        int databaseSizeBeforeUpdate = kitchenRecipesRepository.findAll().size();
        kitchenRecipes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKitchenRecipesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kitchenRecipes))
            )
            .andExpect(status().isBadRequest());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKitchenRecipes() throws Exception {
        int databaseSizeBeforeUpdate = kitchenRecipesRepository.findAll().size();
        kitchenRecipes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKitchenRecipesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(kitchenRecipes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KitchenRecipes in the database
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKitchenRecipes() throws Exception {
        // Initialize the database
        kitchenRecipesRepository.saveAndFlush(kitchenRecipes);

        int databaseSizeBeforeDelete = kitchenRecipesRepository.findAll().size();

        // Delete the kitchenRecipes
        restKitchenRecipesMockMvc
            .perform(delete(ENTITY_API_URL_ID, kitchenRecipes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<KitchenRecipes> kitchenRecipesList = kitchenRecipesRepository.findAll();
        assertThat(kitchenRecipesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
