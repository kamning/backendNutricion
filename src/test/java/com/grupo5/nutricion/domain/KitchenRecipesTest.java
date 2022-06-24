package com.grupo5.nutricion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.grupo5.nutricion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KitchenRecipesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KitchenRecipes.class);
        KitchenRecipes kitchenRecipes1 = new KitchenRecipes();
        kitchenRecipes1.setId(1L);
        KitchenRecipes kitchenRecipes2 = new KitchenRecipes();
        kitchenRecipes2.setId(kitchenRecipes1.getId());
        assertThat(kitchenRecipes1).isEqualTo(kitchenRecipes2);
        kitchenRecipes2.setId(2L);
        assertThat(kitchenRecipes1).isNotEqualTo(kitchenRecipes2);
        kitchenRecipes1.setId(null);
        assertThat(kitchenRecipes1).isNotEqualTo(kitchenRecipes2);
    }
}
