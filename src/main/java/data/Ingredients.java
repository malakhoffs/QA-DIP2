package data;
import lombok.Getter;
import lombok.Setter;
import steps.IngredientSteps;

import java.util.ArrayList;

@Getter @Setter
public class Ingredients extends MainPOJO {

    ArrayList<String> ingredients = new ArrayList<>();
    public Ingredients () {
    }
    public void addIngredients() {
        ingredients.add(IngredientSteps.returnIngredient());
    }

    public void addWrongHashIngredients() {
        ingredients.add("61c5t5v99d1f82001zzzzz00");
        ingredients.add("61c6c5h78d1f82001xxxxx00");
    }

    public void removeIngredients() {
        ingredients.clear();
    }
}


