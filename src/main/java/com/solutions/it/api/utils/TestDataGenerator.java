package com.solutions.it.api.utils;

import com.github.javafaker.Faker;
import com.solutions.it.api.models.Comment;
import com.solutions.it.api.models.Post;
import com.solutions.it.api.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TestDataGenerator - Utility class for generating test data
 * Uses JavaFaker to create realistic test data for API tests
 */
public class TestDataGenerator {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    
    /**
     * Generates a random user with realistic data
     * @return User object
     */
    public static User generateUser() {
        return User.builder()
                .name(faker.name().fullName())
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .website(faker.internet().url())
                .address(generateAddress())
                .company(generateCompany())
                .build();
    }
    
    /**
     * Generates a random address
     * @return Address object
     */
    public static User.Address generateAddress() {
        return User.Address.builder()
                .street(faker.address().streetAddress())
                .suite(faker.address().secondaryAddress())
                .city(faker.address().city())
                .zipcode(faker.address().zipCode())
                .geo(generateGeo())
                .build();
    }
    
    /**
     * Generates random geo coordinates
     * @return Geo object
     */
    public static User.Geo generateGeo() {
        return User.Geo.builder()
                .lat(faker.address().latitude())
                .lng(faker.address().longitude())
                .build();
    }
    
    /**
     * Generates a random company
     * @return Company object
     */
    public static User.Company generateCompany() {
        return User.Company.builder()
                .name(faker.company().name())
                .catchPhrase(faker.company().catchPhrase())
                .bs(faker.company().bs())
                .build();
    }
    
    /**
     * Generates a random post
     * @param userId User ID for the post (optional)
     * @return Post object
     */
    public static Post generatePost(Long userId) {
        return Post.builder()
                .userId(userId != null ? userId : (long) (random.nextInt(10) + 1))
                .title(faker.book().title())
                .body(faker.lorem().paragraph())
                .build();
    }
    
    /**
     * Generates a random comment
     * @param postId Post ID for the comment (optional)
     * @return Comment object
     */
    public static Comment generateComment(Long postId) {
        return Comment.builder()
                .postId(postId != null ? postId : (long) (random.nextInt(100) + 1))
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body(faker.lorem().paragraph())
                .build();
    }
    
    /**
     * Generates a list of random users
     * @param count Number of users to generate
     * @return List of User objects
     */
    public static List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(generateUser());
        }
        return users;
    }
    
    /**
     * Generates a list of random posts
     * @param count Number of posts to generate
     * @param userId User ID for the posts (optional)
     * @return List of Post objects
     */
    public static List<Post> generatePosts(int count, Long userId) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            posts.add(generatePost(userId));
        }
        return posts;
    }
    
    /**
     * Generates a list of random comments
     * @param count Number of comments to generate
     * @param postId Post ID for the comments (optional)
     * @return List of Comment objects
     */
    public static List<Comment> generateComments(int count, Long postId) {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comments.add(generateComment(postId));
        }
        return comments;
    }
} 