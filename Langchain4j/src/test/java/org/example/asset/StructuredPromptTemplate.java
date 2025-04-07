package org.example.asset;

import dev.langchain4j.model.input.structured.StructuredPrompt;

import java.util.List;

public class StructuredPromptTemplate {
    @StructuredPrompt({
            "Create a recipe of a {{dish}} that can be prepared using only {{ingredients}}.",
            "Structure your answer in the following way:",

            "Recipe name: ...",
            "Description: ...",
            "Preparation time: ...",

            "Required ingredients:",
            "- ...",
            "- ...",

            "Instructions:",
            "- ...",
            "- ..."
    })
    public static class CreateRecipePrompt {

        String dish;
        List<String> ingredients;

        public CreateRecipePrompt(String dish, List<String> ingredients) {
            this.dish = dish;
            this.ingredients = ingredients;
        }
    }
}
