package com.example.demo.repository.seed;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.Island.Disposition;
import com.example.demo.repository.entity.Workstation;

@Component
public class SeedRunner implements ApplicationRunner  {

    private final IslandRepository repo;

    SeedRunner(IslandRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("SEMEANDO ----------- \n\n\n");

        inserirIlhaWorkstation();

        List<Island> islands = repo.findIslandWithAvailableWorkstations();

        System.out.println("ISLANDS===========================");
        System.out.println(islands);

        /*
         * Optional<Island> op = repo.findById(1L);
         * 
         * 
         * if (op.isPresent()) {
         * var island = op.get();
         * 
         * // island.getWorkstations();
         * 
         * island.removeWorkstations(
         * ws -> ws.getId().equals("WS1"));
         * 
         * repo.save(island);
         * }
         */
    }

    private void inserirIlhaWorkstation() {
        Island i1 = new Island();
        i1.setDescription("Island 1");
        i1.setDisposition(Disposition.SQUARE);
        i1.setCreatedAt(LocalDateTime.now());
        i1.setUpdatedAt(LocalDateTime.now());

        var ws1 = new Workstation();
        ws1.setId("WS1");
        ws1.setSpecs("Specs ws1");
        ws1.setCreatedAt(LocalDateTime.now());
        ws1.setUpdatedAt(LocalDateTime.now());

        var ws2 = new Workstation();
        ws2.setId("WS2");
        ws2.setSpecs("Specs ws2");
        ws2.setCreatedAt(LocalDateTime.now());
        ws2.setUpdatedAt(LocalDateTime.now());

        // estabelecer o relacionamento nas duas pontas
        ws1.setIsland(i1);
        i1.getWorkstations().add(ws1);

        ws2.setIsland(i1);
        i1.getWorkstations().add(ws2);

        repo.save(i1);

    }
    
}
