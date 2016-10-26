package com.shaun.knowledgetree.services.entities;

import com.shaun.knowledgetree.article.SingularWikiEntity;
import com.shaun.knowledgetree.services.lookup.LookupServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Shaun on 28/05/2016.
 */
@Component
public class WikiEntitiesServicesImpl implements WikiEntitiesServices {

    private LookupServiceImpl lookupServiceImpl = new LookupServiceImpl();

    /**
     * For each entity in the set passed in we aggregate all the children we find and then return them.
     *
     * @param inputEntities : Set of entities to find children for
     * @param rootEntity    : used to set root entity.
     * @return : Set of child entities combined for every element in the input set.
     */
    @Override
    public Set<SingularWikiEntity> aggregateAndReturnChildrenFromSetOfEntities(Set<SingularWikiEntity> inputEntities, SingularWikiEntity rootEntity) {
        Set<SingularWikiEntity> toReturn = new HashSet<>();

        try {
            int count = 1;
            int toDo = inputEntities.size();

            StopWatch stopWatch = new StopWatch();

            for (SingularWikiEntity firstLayerEntity : inputEntities) {
                stopWatch.start(firstLayerEntity.getTitle());
                Set<SingularWikiEntity> childEntities = lookupServiceImpl.findEntities(firstLayerEntity,rootEntity);
                toReturn.addAll(childEntities);
                stopWatch.stop();
                System.out.println(stopWatch.getLastTaskName() + " : " + stopWatch.getLastTaskTimeMillis() + "ms\t|" + "\tProgress = " + count + "/" + toDo);
                count++;
            }

            if (count == 5) {
                System.out.println("stop");
            }

            System.out.println("Finished");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }
}
