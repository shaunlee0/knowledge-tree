package com.shaun.knowledgetree;

import com.shaun.knowledgetree.domain.*;
import com.shaun.knowledgetree.services.MovieService;
import com.shaun.knowledgetree.services.Neo4jServices;
import com.shaun.knowledgetree.services.entities.WikiEntitiesServicesImpl;
import com.shaun.knowledgetree.services.lookup.LookupServiceImpl;
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

/**
 * @author mh
 * @since 06.10.14
 */
@Configuration
@Import(MyNeo4jConfiguration.class)
@RestController("/")
public class SampleMovieApplication extends WebMvcConfigurerAdapter implements CommandLineRunner {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SampleMovieApplication.class, args);
    }

    @Autowired
    MovieService movieService;

    @Autowired
    Neo4jServices neo4jServices;

    @Autowired
    WikiEntitiesServicesImpl wikiEntitiesServicesImpl;

    @Autowired
    LookupServiceImpl lookupServiceImpl;

    @RequestMapping("/graph")
    public Map<String, Object> graph(@RequestParam(value = "limit", required = false) Integer limit) {
        return movieService.graph(limit == null ? 100 : limit);
    }

    @Override
    public void run(String... strings) throws Exception {

        //Find root
        SingularWikiEntity rootEntity = lookupServiceImpl.findRoot("Papal States");
        rootEntity.setDepthFromRoot(0);

        //Trying to work out what the repository does not like... we cant just upload the root entity.
        SingularWikiEntity blankEntity = new SingularWikiEntity();
        SingularWikiEntity blankEntity2 = new SingularWikiEntity();
        blankEntity.setTitle("Blanko wanko");
        blankEntity.setDepthFromRoot(1);
        blankEntity.setRootEntity(blankEntity2);
        blankEntity.setPageContent(new PageContent());
        blankEntity.getPageContent().setEvents(rootEntity.getPageContent().getEvents());
        blankEntity.getPageContent().setCategories(rootEntity.getPageContent().getCategories());
        blankEntity.getPageContent().setHtml(rootEntity.getPageContent().getHtml());
//        blankEntity.getPageContent().setKeyValuesPairs(rootEntity.getPageContent().getKeyValuesPairs());
//        blankEntity.setLinks(rootEntity.getLinks());
//        blankEntity.setPageContent(rootEntity.getPageContent());

        //session.save(rootEntity);

        neo4jServices.saveSingularWikiEntity(blankEntity);
        neo4jServices.saveSingularWikiEntity(rootEntity);

        //SingularWikiEntity rootEntity = lookupServiceImpl.findRoot("Theocracy");

        //Our first layer is only a set of size 10
        Set<SingularWikiEntity> firstEntities = lookupServiceImpl.findEntities(rootEntity, rootEntity);

        firstEntities.forEach(singularWikiEntity -> {
            neo4jServices.saveSingularWikiEntity(singularWikiEntity);
        });

        //Second layer is a set size 100
        Set<SingularWikiEntity> allSecondLayerEntities = wikiEntitiesServicesImpl.getSetOfEntitiesFromWikiEntities(firstEntities, rootEntity);

    }
}
