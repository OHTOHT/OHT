package template.recipe;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import template.recipe.data.Utils;
import template.recipe.model.Recipe;

public class ActivityRecipeDetails extends AppCompatActivity {

    public static final String EXTRA_OBJCT = "template.recipe.OBJ";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Recipe obj) {
        Intent intent = new Intent(activity, ActivityRecipeDetails.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Recipe recipe;
    private FloatingActionButton fab;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_recipe_activity_recipe_details);
        parent_view = findViewById(android.R.id.content);

        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT);

        recipe = (Recipe) getIntent().getSerializableExtra(EXTRA_OBJCT);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabToggle();

        setupToolbar(recipe.getName());

        ((ImageView) findViewById(R.id.image)).setImageResource(recipe.getPhoto());

        LinearLayout ingredients = (LinearLayout) findViewById(R.id.ingredients);
        TextView instructions = (TextView) findViewById(R.id.instructions);

        String[] title_ingredients = getResources().getStringArray(R.array.app_recipe_ingredients);
        addIngredientsList(ingredients, title_ingredients);

        instructions.setText(Html.fromHtml(getString(R.string.app_recipe_instruction)));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isIdExist(getApplicationContext(), recipe.getId() + "")) {
                    Utils.delFavoriteId(getApplicationContext(), recipe.getId() + "");
                    Snackbar.make(parent_view, recipe.getName() + " remove from favorites", Snackbar.LENGTH_SHORT).show();
                } else {
                    Utils.addFavoriteId(getApplicationContext(), recipe.getId() + "");
                    Snackbar.make(parent_view, recipe.getName() + " added to favorites", Snackbar.LENGTH_SHORT).show();
                }
                fabToggle();
            }
        });
    }

    private void setupToolbar(String name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        ((TextView) findViewById(R.id.toolbar_title)).setText(name);
    }

    private void fabToggle() {
        if (Utils.isIdExist(this, recipe.getId() + "")) {
            fab.setImageResource(R.drawable.app_recipe_ic_nav_favorites);
        } else{
            fab.setImageResource(R.drawable.app_recipe_ic_nav_favorites_outline);
        }
    }

    private void addIngredientsList(LinearLayout linearLayout, String[] title){
        for (int i = 0; i < title.length; i++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setTextColor(getResources().getColor(R.color.material_grey_600));
            checkBox.setText(title[i]);
            ll.addView(checkBox);
            linearLayout.addView(ll);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }else{
            Snackbar.make(parent_view, item.getTitle()+" clicked", Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.app_recipe_menu_activity_recipe_details, menu);
        return true;
    }
}
