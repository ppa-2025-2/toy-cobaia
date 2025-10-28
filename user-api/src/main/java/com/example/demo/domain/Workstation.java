package com.example.demo.domain;

import com.example.demo.repository.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "workstations")
public class Workstation extends BaseEntity {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String specs;

    @ManyToOne
    @JoinColumn(name = "island_id")
    private Island island;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public Island getIsland() {
        return island;
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    @Override
    public String toString() {
        return "Workstation [createdAt=" + createdAt
            + ", updatedAt=" + updatedAt
            + ", id=" + id
            + ", specs=" + specs    
            + ", island=" + island
            + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Workstation other = (Workstation) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public void assignUser(User user) {
        if (this.user == null) this.user = user;
        else throw new WorkstationNotAvailableException();
    }

    public class WorkstationNotAvailableException
            extends RuntimeException {};
}
