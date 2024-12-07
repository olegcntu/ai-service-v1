package org.example.aiservicev3.controllers;

import org.example.aiservicev3.service.log_service.LogForAdminService;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/chat-log")
public class ChatLogController {

    private final LogForAdminService logForAdminService;

    public ChatLogController(LogForAdminService logForAdminService) {
        this.logForAdminService = logForAdminService;
    }

    @GetMapping("/get-ai-data")
    public String getChatLog(@RequestParam(value = "email", required = false) String email) {
        return logForAdminService.getHtmlFormattedData(email);
    }
}
