package com.shaun.knowledgetree.services;

import com.shaun.knowledgetree.domain.Graph;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.repositories.GraphMapRepository;
import com.shaun.knowledgetree.repositories.SingularWikiEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Neo4jServices {

    @Autowired
    private SingularWikiEntityRepository singularWikiEntityRepository;

    @Autowired
    private GraphMapRepository graphMapRepository;

    public Neo4jServices() {

    }

    public void saveSingularWikiEntity(SingularWikiEntityDto singularWikiEntityDto) {
        singularWikiEntityRepository.save(singularWikiEntityDto);
    }

    public void saveGraph(Graph graph) {
        graphMapRepository.save(graph);
    }

    public void clearGraph() {
        graphMapRepository.deleteAllNodesAndRelationShips();
    }

    public void removeVerboseRelationships() {
        graphMapRepository.removeRootEntityRelationships();
        graphMapRepository.removeGraphEntitiesRelationships();
        graphMapRepository.removeParentRelationships();
    }
}
