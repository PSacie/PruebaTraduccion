/*package com.myvideogamelist.backend.controller;

import com.myvideogamelist.backend.service.IGDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class IGDBController {

    private final IGDBService igdbService;

    @Autowired
    public IGDBController(IGDBService igdbService) {
        this.igdbService = igdbService;
    }

    @PostMapping("/import")
    public String importGames(@RequestParam String query) {
        return igdbService.fetchAndSaveGames(query);
    }
}*/