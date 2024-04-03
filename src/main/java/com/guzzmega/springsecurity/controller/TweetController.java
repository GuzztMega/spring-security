package com.guzzmega.springsecurity.controller;

import com.guzzmega.springsecurity.domain.Role;
import com.guzzmega.springsecurity.domain.Tweet;
import com.guzzmega.springsecurity.domain.dto.Feed;
import com.guzzmega.springsecurity.domain.dto.FeedItem;
import com.guzzmega.springsecurity.domain.dto.NewTweet;
import com.guzzmega.springsecurity.repository.TweetRepository;
import com.guzzmega.springsecurity.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/tweets")
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Void> createTweet(@RequestBody NewTweet newTweet, JwtAuthenticationToken token){
        var user = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        var tweet = new Tweet();
        tweet.setUser(user);
        tweet.setContent(newTweet.content());

        tweetRepository.save(tweet);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<Feed> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        var tweets = tweetRepository.findAll(
                PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(tweet -> new FeedItem(
                        tweet.getTweetId(),
                        tweet.getContent(),
                        tweet.getUser().getUsername()
                ));

        return ResponseEntity.ok(new Feed(tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token){
        var tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var user = userRepository.findById(UUID.fromString(token.getName()));
        var idAdmin = user.get().getRoleSet()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Type.ADMIN.name()));

        if(idAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))){
            tweetRepository.delete(tweet);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
