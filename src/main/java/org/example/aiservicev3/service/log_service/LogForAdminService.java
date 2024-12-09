package org.example.aiservicev3.service.log_service;

import org.example.aiservicev3.service.MessageService;
import org.example.aiservicev3.entity.MessageRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogForAdminService {
    private final MessageService messageService;

    public LogForAdminService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String getHtmlFormattedData(String email) {
        List<MessageRecord> messages;

        if (email != null && !email.isEmpty()) {
            messages = messageService.findAllMessagesByEmailSortedByTime(email);
        } else {
            messages = messageService.findAllMessagesSortedByTime();
        }

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><head>");
        htmlBuilder.append("<style>")
                .append("table {border-collapse: collapse; width: 100%;}")
                .append("th, td {border: 1px solid #ddd; padding: 8px; text-align: left;}")
                .append("th {background-color: #f2f2f2;}")
                .append("</style>");
        htmlBuilder.append("</head><body>");
        htmlBuilder.append("<h1>Chat Logs</h1>");
        htmlBuilder.append("<table>");
        htmlBuilder.append("<tr><th>ID</th><th>Message</th><th>Response</th><th>Recommendation</th><th>Email</th><th>Timestamp</th></tr>");

        for (MessageRecord message : messages) {
            htmlBuilder.append("<tr>")
                    .append("<td>").append(message.getId()).append("</td>")
                    .append("<td>").append(escapeHtml(message.getMessage())).append("</td>")
                    .append("<td>").append(escapeHtml(message.getResponse())).append("</td>")
                    .append("<td>").append(escapeHtml(message.getRecommendation())).append("</td>")
                    .append("<td>").append(escapeHtml(message.getEmail())).append("</td>")
                    .append("<td>").append(message.getTimestamp()).append("</td>")
                    .append("</tr>");
        }

        htmlBuilder.append("</table>");
        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
