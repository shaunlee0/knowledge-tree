package com.shaun.knowledgetree.services;

import com.shaun.knowledgetree.domain.SingularWikiEntity;
import com.shaun.knowledgetree.domain.SingularWikiEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SingularWikiEntityDtoBuilder {

    @Autowired
    private PageContentDtoBuilder pageContentDtoBuilder;

    public SingularWikiEntityDto convert(SingularWikiEntity singularWikiEntity) {
        SingularWikiEntityDto singularWikiEntityDto = new SingularWikiEntityDto();
        singularWikiEntityDto.setPage(singularWikiEntity.getPage());
        singularWikiEntityDto.setTitle(singularWikiEntity.getTitle());
        singularWikiEntityDto.setLinks(singularWikiEntity.getPageContent().getLinks());
        singularWikiEntityDto.setExternalLinks(singularWikiEntity.getExternalLinks());
        singularWikiEntityDto.setDepthFromRoot(singularWikiEntity.getDepthFromRoot());
        singularWikiEntityDto.setPageContentDto(pageContentDtoBuilder.convert(singularWikiEntity.getPageContent()));
        singularWikiEntityDto.setRootEntity(convertAvoidRecursion(singularWikiEntity.getRootEntity()));
        singularWikiEntityDto.setParent(convertAvoidRecursion(singularWikiEntity.getParent()));
        return singularWikiEntityDto;
    }

    public SingularWikiEntityDto convertRoot(SingularWikiEntity singularWikiEntity) {
        SingularWikiEntityDto singularWikiEntityDto = new SingularWikiEntityDto();
        singularWikiEntityDto.setTitle(singularWikiEntity.getTitle());
        singularWikiEntityDto.setLinks(singularWikiEntity.getPageContent().getLinks());
        singularWikiEntityDto.setExternalLinks(singularWikiEntity.getExternalLinks());
        singularWikiEntityDto.setDepthFromRoot(singularWikiEntity.getDepthFromRoot());
        singularWikiEntityDto.setPageContentDto(pageContentDtoBuilder.convert(singularWikiEntity.getPageContent()));
        return singularWikiEntityDto;
    }

    private SingularWikiEntityDto convertAvoidRecursion(SingularWikiEntity singularWikiEntity) {
        SingularWikiEntityDto singularWikiEntityDto = new SingularWikiEntityDto();
        singularWikiEntityDto.setPage(singularWikiEntity.getPage());
        singularWikiEntityDto.setTitle(singularWikiEntity.getTitle());
        singularWikiEntityDto.setLinks(singularWikiEntity.getPageContent().getLinks());
        singularWikiEntityDto.setExternalLinks(singularWikiEntity.getExternalLinks());
        singularWikiEntityDto.setDepthFromRoot(singularWikiEntity.getDepthFromRoot());
        singularWikiEntityDto.setPageContentDto(pageContentDtoBuilder.convert(singularWikiEntity.getPageContent()));
        return singularWikiEntityDto;
    }

}
