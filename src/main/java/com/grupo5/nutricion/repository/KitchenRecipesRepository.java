package com.grupo5.nutricion.repository;

import com.grupo5.nutricion.domain.KitchenRecipes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the KitchenRecipes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KitchenRecipesRepository extends JpaRepository<KitchenRecipes, Long> {}
