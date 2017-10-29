package co.uk.fernandopinto.aco.algo;

/**
 * Created by Fernando on 20/09/2017.
 */
import co.uk.fernandopinto.aco.App;
import co.uk.fernandopinto.aco.view.Builder;
import co.uk.fernandopinto.aco.view.GraphBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.util.List;

public class ProjectModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(Individual.class).to(Ant.class);
        bind(PathBuilder.class).to(Tour.class);
        bind(Builder.class).to(GraphBuilder.class);
    }

    @Provides
    List<float[][]> provideEdges(App app) {

        return app.getEdges();
    }

    @Provides
    List<Location> provideLocations(App app) {

        return app.getLocations();
    }
}
