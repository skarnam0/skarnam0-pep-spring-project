package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
@RequestMapping
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    // registering new user
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if (account == null || account.getUsername() == null || account.getUsername().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (accountService.existsByUsername(account.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (accountService.registerAccount(account) == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    // logging in a user
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedInAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
        if (loggedInAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(loggedInAccount);
    }

    // creating a new message
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if (!accountService.existsAccount(message.getPostedBy())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (message.getPostedBy() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(messageService.createMessage(message));
    }

    // get all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.findAllMessages());
    }

    // get messages by user
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        return ResponseEntity.ok(messageService.findByAccount(accountId));
    }

    // get message by id
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return ResponseEntity.ok(messageService.findById(messageId));
    }

    // delete a message
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        boolean messageExists = messageService.findById(messageId) != null;

        if (messageExists) {
            messageService.deleteMessage(messageId);
            return ResponseEntity.ok().body(1);
        } else {
            return ResponseEntity.ok().build();
        }
        
    }

    // update a message
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (message.getMessageText().length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Message updatedMessage = messageService.updateMessage(messageId, message.getMessageText());
        if (updatedMessage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(1);
    }

}
