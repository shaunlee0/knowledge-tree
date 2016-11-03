package com.shaun.knowledgetree;

import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.Relationship;
import com.shaun.knowledgetree.article.SingularWikiEntity;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.services.MovieService;
import com.shaun.knowledgetree.services.Neo4jServices;
import com.shaun.knowledgetree.services.pageContent.PageContentService;
import com.shaun.knowledgetree.util.SingularWikiEntityDtoBuilder;
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

    @Autowired
    PageContentService pageContentService;

    @RequestMapping("/graph")
    public Map<String, Object> graph(@RequestParam(value = "limit", required = false) Integer limit) {
        return movieService.graph(limit == null ? 100 : limit);
    }

    @Override
    public void run(String... strings) throws Exception {

        neo4jServices.clearGraph();
        String searchTerm = "Papal States";
        Common.setGraph(new Graph(searchTerm));

        //Find root
        SingularWikiEntity rootEntity = lookupServiceImpl.findRoot(searchTerm);
        rootEntity.setDepthFromRoot(0);

        SingularWikiEntityDto rootEntityDto = singularWikiEntityDtoBuilder.convertRoot(rootEntity);
        Common.setRootEntity(rootEntityDto);
        Common.getGraph().getEntities().add(rootEntityDto);

        //Our first layer is only a set of size 10
        Set<SingularWikiEntity> firstEntities = lookupServiceImpl.findEntities(rootEntity, rootEntity);

        //For each wiki entity hanging off the root(first entities) convert it and add it to the graph
        firstEntities.forEach(singularWikiEntity -> {
            Common.getGraph().getEntities().add(singularWikiEntityDtoBuilder.convert(singularWikiEntity));
        });

        //Second layer is a set size 100, converting all these and adding to graph
        Set<SingularWikiEntity> allSecondLayerEntities = wikiEntitiesServicesImpl.aggregateAndReturnChildrenFromSetOfEntities(firstEntities, rootEntity);
        allSecondLayerEntities.forEach(singularWikiEntity -> {
            Common.getGraph().getEntities().add(singularWikiEntityDtoBuilder.convert(singularWikiEntity));
        });

        Common.getGraph().getEntities().forEach(singularWikiEntityDto -> {
            if (singularWikiEntityDto.getParent() != null) {

                //Establish parent to child relationship
                Relationship parentToChildRelationship = new Relationship(singularWikiEntityDto.getParent(), singularWikiEntityDto);
                pageContentService.extractRelationshipContentFromPageContent(parentToChildRelationship);
                singularWikiEntityDto.getParent().getRelatedEntities().add(parentToChildRelationship);

                //Establish child to parent relationship
                Relationship childToParentRelationship = new Relationship(singularWikiEntityDto, singularWikiEntityDto.getParent());
                pageContentService.extractRelationshipContentFromPageContent(childToParentRelationship);
                singularWikiEntityDto.getRelatedEntities().add(childToParentRelationship);
            }
        });

        neo4jServices.saveGraph(Common.getGraph());
        neo4jServices.removeVerboseRelationships();
        System.out.println("Graph saved.");
        Common.findLinksAndOccurences();
    }
}
