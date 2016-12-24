package com.shaun.knowledgetree;

import com.shaun.knowledgetree.config.MyNeo4jConfiguration;
import com.shaun.knowledgetree.services.neo4j.MovieService;
import com.shaun.knowledgetree.services.Neo4jServices;
import com.shaun.knowledgetree.services.entities.WikiEntitiesServicesImpl;
import com.shaun.knowledgetree.services.lookup.LookupService;
import com.shaun.knowledgetree.services.pageContent.PageContentService;
import com.shaun.knowledgetree.util.SingularWikiEntityDtoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.util.Map;

@Configuration
@Import(MyNeo4jConfiguration.class)
@RestController("/")
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

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
    LookupService lookupService;

    @Autowired
    SingularWikiEntityDtoBuilder singularWikiEntityDtoBuilder;

    @Autowired
    PageContentService pageContentService;

    @RequestMapping("/graph-to-d3")
    public Map<String, Object> graph(@RequestParam(value = "limit", required = false) Integer limit) {
        return movieService.graph(limit == null ? 100 : limit);
    }

}
