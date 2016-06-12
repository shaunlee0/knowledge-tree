package com.shaun.knowledgetree.services;

import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.repositories.SingularWikiEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Neo4jServices {

    @Autowired
    private SingularWikiEntityRepository singularWikiEntityRepository;

    public Neo4jServices() {

    }

    public void saveSingularWikiEntity(SingularWikiEntityDto singularWikiEntityDto) {
        singularWikiEntityRepository.save(singularWikiEntityDto);
    }
}
