package com.grupo5.nutricion.web.rest;

import com.grupo5.nutricion.domain.KitchenRecipes;
import com.grupo5.nutricion.repository.KitchenRecipesRepository;
import com.grupo5.nutricion.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.grupo5.nutricion.domain.KitchenRecipes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class KitchenRecipesResource {

    private final Logger log = LoggerFactory.getLogger(KitchenRecipesResource.class);

    private static final String ENTITY_NAME = "kitchenRecipes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KitchenRecipesRepository kitchenRecipesRepository;

    public KitchenRecipesResource(KitchenRecipesRepository kitchenRecipesRepository) {
        this.kitchenRecipesRepository = kitchenRecipesRepository;
    }

    /**
     * {@code POST  /kitchen-recipes} : Create a new kitchenRecipes.
     *
     * @param kitchenRecipes the kitchenRecipes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kitchenRecipes, or with status {@code 400 (Bad Request)} if the kitchenRecipes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kitchen-recipes")
    public ResponseEntity<KitchenRecipes> createKitchenRecipes(@Valid @RequestBody KitchenRecipes kitchenRecipes)
        throws URISyntaxException {
        log.debug("REST request to save KitchenRecipes : {}", kitchenRecipes);
        if (kitchenRecipes.getId() != null) {
            throw new BadRequestAlertException("A new kitchenRecipes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KitchenRecipes result = kitchenRecipesRepository.save(kitchenRecipes);
        return ResponseEntity
            .created(new URI("/api/kitchen-recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kitchen-recipes/:id} : Updates an existing kitchenRecipes.
     *
     * @param id the id of the kitchenRecipes to save.
     * @param kitchenRecipes the kitchenRecipes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kitchenRecipes,
     * or with status {@code 400 (Bad Request)} if the kitchenRecipes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kitchenRecipes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kitchen-recipes/{id}")
    public ResponseEntity<KitchenRecipes> updateKitchenRecipes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KitchenRecipes kitchenRecipes
    ) throws URISyntaxException {
        log.debug("REST request to update KitchenRecipes : {}, {}", id, kitchenRecipes);
        if (kitchenRecipes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kitchenRecipes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kitchenRecipesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        KitchenRecipes result = kitchenRecipesRepository.save(kitchenRecipes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kitchenRecipes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /kitchen-recipes/:id} : Partial updates given fields of an existing kitchenRecipes, field will ignore if it is null
     *
     * @param id the id of the kitchenRecipes to save.
     * @param kitchenRecipes the kitchenRecipes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kitchenRecipes,
     * or with status {@code 400 (Bad Request)} if the kitchenRecipes is not valid,
     * or with status {@code 404 (Not Found)} if the kitchenRecipes is not found,
     * or with status {@code 500 (Internal Server Error)} if the kitchenRecipes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/kitchen-recipes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KitchenRecipes> partialUpdateKitchenRecipes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KitchenRecipes kitchenRecipes
    ) throws URISyntaxException {
        log.debug("REST request to partial update KitchenRecipes partially : {}, {}", id, kitchenRecipes);
        if (kitchenRecipes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kitchenRecipes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kitchenRecipesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KitchenRecipes> result = kitchenRecipesRepository
            .findById(kitchenRecipes.getId())
            .map(existingKitchenRecipes -> {
                if (kitchenRecipes.getName() != null) {
                    existingKitchenRecipes.setName(kitchenRecipes.getName());
                }
                if (kitchenRecipes.getDescription() != null) {
                    existingKitchenRecipes.setDescription(kitchenRecipes.getDescription());
                }
                if (kitchenRecipes.getIngredients() != null) {
                    existingKitchenRecipes.setIngredients(kitchenRecipes.getIngredients());
                }
                if (kitchenRecipes.getPreparation() != null) {
                    existingKitchenRecipes.setPreparation(kitchenRecipes.getPreparation());
                }
                if (kitchenRecipes.getImage() != null) {
                    existingKitchenRecipes.setImage(kitchenRecipes.getImage());
                }
                if (kitchenRecipes.getImageContentType() != null) {
                    existingKitchenRecipes.setImageContentType(kitchenRecipes.getImageContentType());
                }
                if (kitchenRecipes.getStatus() != null) {
                    existingKitchenRecipes.setStatus(kitchenRecipes.getStatus());
                }

                return existingKitchenRecipes;
            })
            .map(kitchenRecipesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kitchenRecipes.getId().toString())
        );
    }

    /**
     * {@code GET  /kitchen-recipes} : get all the kitchenRecipes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kitchenRecipes in body.
     */
    @GetMapping("/kitchen-recipes")
    public List<KitchenRecipes> getAllKitchenRecipes() {
        log.debug("REST request to get all KitchenRecipes");
        return kitchenRecipesRepository.findAll();
    }

    /**
     * {@code GET  /kitchen-recipes/:id} : get the "id" kitchenRecipes.
     *
     * @param id the id of the kitchenRecipes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kitchenRecipes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kitchen-recipes/{id}")
    public ResponseEntity<KitchenRecipes> getKitchenRecipes(@PathVariable Long id) {
        log.debug("REST request to get KitchenRecipes : {}", id);
        Optional<KitchenRecipes> kitchenRecipes = kitchenRecipesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(kitchenRecipes);
    }

    /**
     * {@code DELETE  /kitchen-recipes/:id} : delete the "id" kitchenRecipes.
     *
     * @param id the id of the kitchenRecipes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kitchen-recipes/{id}")
    public ResponseEntity<Void> deleteKitchenRecipes(@PathVariable Long id) {
        log.debug("REST request to delete KitchenRecipes : {}", id);
        kitchenRecipesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
