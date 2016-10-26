package com.shaun.knowledgetree.domain;

import org.neo4j.ogm.annotation.*;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import org.neo4j.ogm.annotation.Relationship;

//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonIdentityInfo(generator=JSOGGenerator.class)
// tag::movie[]
@NodeEntity
public class Movie {
    @GraphId Long id;

    private String title;

    private int released;
    private String tagline;

    @org.neo4j.ogm.annotation.Relationship(type = "ACTED_IN", direction = Relationship.INCOMING)
    private List<Role> roles;

// end::movie[]

    public Movie() { }

    public String getTitle() {
        return title;
    }

    public int getReleased() {
        return released;
    }

    public String getTagline() {
        return tagline;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleased(int released) {
        this.released = released;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
    
}
