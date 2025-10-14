package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.Island.Disposition;

public interface IslandRepository 
    extends ListCrudRepository<Island, Long> {

    List<Island> findByDisposition(Disposition disposition);
 
    /*
     * GROUP BY w
     * HAVING w.user IS NULL
     */
    @Query(value = """
            SELECT i 
            FROM Island i JOIN i.workstations w
            WHERE w.user IS NULL
            """)
    List<Island> findIslandWithAvailableWorkstations();

}
