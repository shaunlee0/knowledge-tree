package com.shaun.knowledgetree.services;

import com.shaun.knowledgetree.model.SingularWikiEntity;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import com.shaun.knowledgetree.util.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SingularWikiEntityDtoBuilder {

    @Autowired
    private PageContentDtoBuilder pageContentDtoBuilder;


    public SingularWikiEntityDto convert(SingularWikiEntity singularWikiEntity) {
        SingularWikiEntityDto singularWikiEntityDto;
        //if not in all entities create it and add it to it
        if (!Common.allEntities.containsKey(singularWikiEntity.getTitle())) {
            singularWikiEntityDto = new SingularWikiEntityDto();
            singularWikiEntityDto.setTitle(singularWikiEntity.getTitle());
            singularWikiEntityDto.getPageContent().setLinks(singularWikiEntity.getPageContent().getLinks());
            singularWikiEntityDto.setExternalLinks(singularWikiEntity.getExternalLinks());
            singularWikiEntityDto.setDepthFromRoot(singularWikiEntity.getDepthFromRoot());
            singularWikiEntityDto.setPageContentDto(pageContentDtoBuilder.convert(singularWikiEntity.getPageContent()));
            singularWikiEntityDto.setRootEntity(Common.getRootEntity());
            singularWikiEntityDto.setParent(retrieveParent(singularWikiEntity.getParent()));
            Common.allEntities.put(singularWikiEntityDto.getTitle(), singularWikiEntityDto);
        }
        //Otherwise just grab it.
        else {
            singularWikiEntityDto = Common.allEntities.get(singularWikiEntity.getTitle());
        }
        return singularWikiEntityDto;
    }

    public SingularWikiEntityDto convertRoot(SingularWikiEntity singularWikiEntity) {
        SingularWikiEntityDto rootEntityDto = new SingularWikiEntityDto();
        rootEntityDto.setTitle(singularWikiEntity.getTitle());
        rootEntityDto.getPageContent().setLinks(singularWikiEntity.getPageContent().getLinks());
        rootEntityDto.setExternalLinks(singularWikiEntity.getExternalLinks());
        rootEntityDto.setDepthFromRoot(singularWikiEntity.getDepthFromRoot());
        rootEntityDto.setPageContentDto(pageContentDtoBuilder.convert(singularWikiEntity.getPageContent()));
        Common.setRootEntity(rootEntityDto);
        return rootEntityDto;
    }

    private SingularWikiEntityDto retrieveParent(SingularWikiEntity singularWikiEntity) {
        SingularWikiEntityDto singularWikiEntityDto;
        if (!Common.allEntities.containsKey(singularWikiEntity.getTitle())) {
            System.out.println("We created a parent wtf is going on here?");
            singularWikiEntityDto = new SingularWikiEntityDto();
            singularWikiEntityDto.setTitle(singularWikiEntity.getTitle());
            singularWikiEntityDto.getPageContent().setLinks(singularWikiEntity.getPageContent().getLinks());
            singularWikiEntityDto.setExternalLinks(singularWikiEntity.getExternalLinks());
            singularWikiEntityDto.setDepthFromRoot(singularWikiEntity.getDepthFromRoot());
            singularWikiEntityDto.setPageContentDto(pageContentDtoBuilder.convert(singularWikiEntity.getPageContent()));
            Common.allEntities.put(singularWikiEntityDto.getTitle(), singularWikiEntityDto);
        } else {
            singularWikiEntityDto = Common.allEntities.get(singularWikiEntity.getTitle());
        }

        return singularWikiEntityDto;
    }

}
