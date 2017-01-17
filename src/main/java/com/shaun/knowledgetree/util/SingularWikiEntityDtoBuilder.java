package com.shaun.knowledgetree.util;

import com.shaun.knowledgetree.domain.SingularWikiEntity;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SingularWikiEntityDtoBuilder {

    @Autowired
    private PageContentDtoBuilder pageContentDtoBuilder;


    public SingularWikiEntityDto convert(SingularWikiEntity singularWikiEntity) {
        SingularWikiEntityDto singularWikiEntityDto;
        //if not in all entities create it and add it to it
        if (!SharedSearchStorage.allEntities.containsKey(singularWikiEntity.getTitle())) {
            singularWikiEntityDto = new SingularWikiEntityDto();
            singularWikiEntityDto.setTitle(singularWikiEntity.getTitle());
            singularWikiEntityDto.getPageContent().setLinks(singularWikiEntity.getPageContent().getLinks());
            singularWikiEntityDto.setExternalLinks(singularWikiEntity.getExternalLinks());
            singularWikiEntityDto.setDepthFromRoot(singularWikiEntity.getDepthFromRoot());
            singularWikiEntityDto.setPageContentDto(pageContentDtoBuilder.convert(singularWikiEntity.getPageContent()));
            singularWikiEntityDto.setRootEntity(SharedSearchStorage.getRootEntity());
            singularWikiEntityDto.setParent(retrieveParent(singularWikiEntity.getParent()));
            singularWikiEntityDto.setRelatedEntities(singularWikiEntity.getRelatedEntities());
            SharedSearchStorage.allEntities.put(singularWikiEntityDto.getTitle(), singularWikiEntityDto);
        }
        //Otherwise just grab it.
        else {
            singularWikiEntityDto = SharedSearchStorage.allEntities.get(singularWikiEntity.getTitle());
        }
        return singularWikiEntityDto;
    }

    public SingularWikiEntityDto convertRoot(SingularWikiEntity singularWikiEntity) {
        SingularWikiEntityDto rootEntityDto = new SingularWikiEntityDto();
        rootEntityDto.setTitle(singularWikiEntity.getTitle());
        rootEntityDto.getPageContent().setLinks(singularWikiEntity.getPageContent().getLinks());
        rootEntityDto.setExternalLinks(singularWikiEntity.getExternalLinks());
        rootEntityDto.setDepthFromRoot(singularWikiEntity.getDepthFromRoot());
        rootEntityDto.setIsRoot(singularWikiEntity.isRoot());
        rootEntityDto.setPageContentDto(pageContentDtoBuilder.convert(singularWikiEntity.getPageContent()));
        SharedSearchStorage.setRootEntity(rootEntityDto);
        return rootEntityDto;
    }

    private SingularWikiEntityDto retrieveParent(SingularWikiEntity singularWikiEntity) {
        SingularWikiEntityDto singularWikiEntityDto;
        if (!SharedSearchStorage.allEntities.containsKey(singularWikiEntity.getTitle())) {
            singularWikiEntityDto = new SingularWikiEntityDto();
            singularWikiEntityDto.setTitle(singularWikiEntity.getTitle());
            singularWikiEntityDto.getPageContent().setLinks(singularWikiEntity.getPageContent().getLinks());
            singularWikiEntityDto.setExternalLinks(singularWikiEntity.getExternalLinks());
            singularWikiEntityDto.setDepthFromRoot(singularWikiEntity.getDepthFromRoot());
            singularWikiEntityDto.setPageContentDto(pageContentDtoBuilder.convert(singularWikiEntity.getPageContent()));
            SharedSearchStorage.allEntities.put(singularWikiEntityDto.getTitle(), singularWikiEntityDto);
        } else {
            singularWikiEntityDto = SharedSearchStorage.allEntities.get(singularWikiEntity.getTitle());
        }

        return singularWikiEntityDto;
    }

}
