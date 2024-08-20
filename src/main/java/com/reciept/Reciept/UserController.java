package com.reciept.Reciept;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<User> users = userService.loadUsers(); // Replace with your method to get users
        model.addAttribute("users", users);
        return "index"; // This should match your Thymeleaf template name without .html
    }

    @PostMapping("/add_user")
    public String addUser(@RequestParam String username) {
        userService.addUser(username);
        return "redirect:/";
    }

    @GetMapping("/delete_user/{username}")
    public String deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return "redirect:/";
    }
    @GetMapping("/generate_receipt/{username}")
    public ResponseEntity<byte[]> generateReceipt(@PathVariable String username) {
        if (!userService.loadUsers().stream().anyMatch(user -> user.getUsername().equals(username))) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph("Donation Receipt"));
            document.add(new Paragraph("Thank you for your donation!"));
            document.add(new Paragraph("User: " + username));
            document.add(new Paragraph("Amount: $100"));
            document.add(new Paragraph("Date: " + java.time.LocalDate.now()));
            document.add(new Paragraph("Best regards, Kohinoor Masjid"));
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + username + "_receipt.pdf");
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }
}
