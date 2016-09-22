package com.shaun.knowledgetree;

import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.model.SingularWikiEntity;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.MovieService;
import com.shaun.knowledgetree.services.Neo4jServices;
import com.shaun.knowledgetree.services.SingularWikiEntityDtoBuilder;
import com.shaun.knowledgetree.services.entities.WikiEntitiesServicesImpl;
import com.shaun.knowledgetree.services.lookup.LookupServiceImpl;
import com.shaun.knowledgetree.util.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Configuration
@Import(MyNeo4jConfiguration.class)
@RestController("/")
public class Application extends WebMvcConfigurerAdapter implements CommandLineRunner {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    MovieService movieService;

    @Autowired
    Neo4jServices neo4jServices;

    @Autowired
    WikiEntitiesServicesImpl wikiEntitiesServicesImpl;

    @Autowired
    LookupServiceImpl lookupServiceImpl;

    @Autowired
    SingularWikiEntityDtoBuilder singularWikiEntityDtoBuilder;

    @RequestMapping("/graph")
    public Map<String, Object> graph(@RequestParam(value = "limit", required = false) Integer limit) {
        return movieService.graph(limit == null ? 100 : limit);
    }

    @Override
    public void run(String... strings) throws Exception {

        String searchTerm = "Papal States";

        Common.setGraph(new Graph(searchTerm));

        //Find root
        SingularWikiEntity rootEntity = lookupServiceImpl.findRoot(searchTerm);
        rootEntity.setDepthFromRoot(0);

        SingularWikiEntityDto rootEntityDto = singularWikiEntityDtoBuilder.convertRoot(rootEntity);
        Common.getGraph().getEntities().add(rootEntityDto);

//        neo4jServices.saveSingularWikiEntity(rootEntityDto);

        //SingularWikiEntity rootEntity = lookupServiceImpl.findRoot("Theocracy");

        //Our first layer is only a set of size 10
        Set<SingularWikiEntity> firstEntities = lookupServiceImpl.findEntities(rootEntity, rootEntity);

        firstEntities.forEach(singularWikiEntity -> {
            Common.getGraph().getEntities().add(singularWikiEntityDtoBuilder.convert(singularWikiEntity));
//            neo4jServices.saveSingularWikiEntity(singularWikiEntityDtoBuilder.convert(singularWikiEntity));
        });

        neo4jServices.saveGraph(Common.getGraph());
        System.out.println("Graph saved.");

        Common.findLinksAndOccurences();

        //Second layer is a set size 100
//        Set<SingularWikiEntity> allSecondLayerEntities = wikiEntitiesServicesImpl.getSetOfEntitiesFromWikiEntities(firstEntities, rootEntity);

    }
}
