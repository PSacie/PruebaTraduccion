package com.myvideogamelist.backend.controller;

import com.myvideogamelist.backend.dto.TranslateRequestDTO;
import com.myvideogamelist.backend.dto.TranslateResponseDTO;
//import com.myvideogamelist.backend.service.GameInListService;
import com.myvideogamelist.backend.service.TranslationService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translate")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;
    
    @GetMapping("/api/test")
    public String test() {
        return "Backend funcionando";
    }

    @PostMapping
    public TranslateResponseDTO translateToSpanish(@RequestBody TranslateRequestDTO request) {
        System.out.println(">>> TRADUCCIÓN RECIBIDA: " + request.getText());
        return translationService.translateToSpanish(request.getText());
    }
}