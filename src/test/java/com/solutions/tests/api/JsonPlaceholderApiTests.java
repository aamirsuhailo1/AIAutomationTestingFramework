package com.solutions.tests.api;

import com.solutions.it.api.models.Comment;
import com.solutions.it.api.models.Post;
import com.solutions.it.api.models.User;
import com.solutions.it.api.utils.TestDataGenerator;
import com.solutions.it.api.utils.TestDataManager;
import com.solutions.it.api.validators.ResponseValidator;
import com.solutions.it.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * JsonPlaceholderApiTests - API tests for JSONPlaceholder API
 * Contains tests for all HTTP methods and various response codes
 */
public class JsonPlaceholderApiTests extends BaseApiTest {

    // ==================== GET TESTS ====================
    
    /**
     * Test to get all users
     */
    @Test(groups = {"smoke", "api", "get"})
    public void testGetAllUsers() {
        // Execute request
        Response response = apiClient.get(getEndpointUrl("users"));
        
        // Log response
        logResponseToReport(response);
        
        // Validate response
        validateSuccessResponse(response, 200);
        ResponseValidator.validateArraySize(response, "", 10); // JSONPlaceholder has 10 users
        
        // Extract users from response
        List<User> users = response.jsonPath().getList("", User.class);
        Assert.assertNotNull(users, "Users list should not be null");
        Assert.assertTrue(users.size() > 0, "Users list should not be empty");
        
        // Verify a user's data
        User firstUser = users.get(0);
        Assert.assertNotNull(firstUser.getId(), "User ID should not be null");
        Assert.assertNotNull(firstUser.getName(), "User name should not be null");
        Assert.assertNotNull(firstUser.getEmail(), "User email should not be null");
        
        extentTest.pass("Successfully verified GET all users");
    }
    
    /**
     * Test to get a single user by ID
     */
    @Test(groups = {"regression", "api", "get"})
    public void testGetUserById() {
        // Test data
        int userId = 1;
        
        // Execute request
        Response response = apiClient.get(getEndpointUrl("users") + "/" + userId);
        
        // Log response
        logResponseToReport(response);
        
        // Validate response
        validateSuccessResponse(response, 200);
        
        // Verify user data
        User user = response.as(User.class);
        Assert.assertEquals(user.getId().intValue(), userId, "User ID should match the requested ID");
        Assert.assertNotNull(user.getName(), "User name should not be null");
        Assert.assertNotNull(user.getEmail(), "User email should not be null");
        
        extentTest.pass("Successfully verified GET user by ID");
    }
    
    /**
     * Test to get a user that doesn't exist
     */
    @Test(groups = {"negative", "api", "get"})
    public void testGetNonExistentUser() {
        // Test data
        int userId = 9999; // Non-existent user ID
        
        // Execute request
        Response response = apiClient.get(getEndpointUrl("users") + "/" + userId);
        
        // Log response
        logResponseToReport(response);
        
        // Validate 404 response
        ResponseValidator.validateStatusCode(response, 404);
        
        // Verify empty response body
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.trim().isEmpty() || responseBody.equals("{}"), 
                "Response body should be empty or an empty JSON object");
        
        extentTest.pass("Successfully verified 404 for non-existent user");
    }
    
    /**
     * Test to get posts for a specific user
     */
    @Test(groups = {"regression", "api", "get"})
    public void testGetPostsByUserId() {
        // Test data
        int userId = 1;
        
        // Execute request with query parameters
        Map<String, String> queryParams = createQueryParams("userId", userId);
        Response response = apiClient.get(getEndpointUrl("posts"), queryParams);
        
        // Log response
        logResponseToReport(response);
        
        // Validate response
        validateSuccessResponse(response, 200);
        
        // Verify posts belong to specified user
        List<Post> posts = response.jsonPath().getList("", Post.class);
        Assert.assertFalse(posts.isEmpty(), "Posts list should not be empty");
        
        for (Post post : posts) {
            Assert.assertEquals(post.getUserId().intValue(), userId, 
                    "All posts should belong to user ID " + userId);
        }
        
        extentTest.pass("Successfully verified GET posts by user ID");
    }

    // ==================== POST TESTS ====================
    
    /**
     * Test to create a new user
     */
    @Test(groups = {"smoke", "api", "post"})
    public void testCreateNewUser() {
        // Generate test user data
        User newUser = TestDataGenerator.generateUser();
        newUser.setId(null); // Let the server assign an ID
        
        // Execute request
        Response response = apiClient.post(getEndpointUrl("users"), newUser);
        
        // Log response
        logResponseToReport(response);
        
        // Validate response - JSONPlaceholder returns 201 for POST
        validateSuccessResponse(response, 201);
        
        // Verify created user data
        User createdUser = response.as(User.class);
        Assert.assertNotNull(createdUser.getId(), "Created user should have an ID");
        Assert.assertEquals(createdUser.getName(), newUser.getName(), "User name should match");
        Assert.assertEquals(createdUser.getEmail(), newUser.getEmail(), "User email should match");
        
        extentTest.pass("Successfully verified POST new user");
    }
    
    /**
     * Test to create a new post with JSON test data
     */
    @Test(groups = {"regression", "api", "post"})
    public void testCreateNewPostWithJsonData() {
        // Load test data from JSON file
        Post newPost = TestDataManager.loadJsonFile("posts.json", Post[].class)[0];
        newPost.setId(null); // Let the server assign an ID
        
        // Execute request
        Response response = apiClient.post(getEndpointUrl("posts"), newPost);
        
        // Log response
        logResponseToReport(response);
        
        // Validate response
        validateSuccessResponse(response, 201);
        
        // Verify created post data
        Post createdPost = response.as(Post.class);
        Assert.assertNotNull(createdPost.getId(), "Created post should have an ID");
        Assert.assertEquals(createdPost.getTitle(), newPost.getTitle(), "Post title should match");
        Assert.assertEquals(createdPost.getBody(), newPost.getBody(), "Post body should match");
        
        extentTest.pass("Successfully verified POST new post with JSON data");
    }

    // ==================== PUT TESTS ====================
    
    /**
     * Test to update a user completely
     */
    @Test(groups = {"regression", "api", "put"})
    public void testUpdateUserComplete() {
        // Test data
        int userId = 1;
        User updatedUser = TestDataGenerator.generateUser();
        updatedUser.setId((long) userId);
        
        // Execute request
        Response response = apiClient.put(getEndpointUrl("users") + "/" + userId, updatedUser);
        
        // Log response
        logResponseToReport(response);
        
        // Validate response
        validateSuccessResponse(response, 200);
        
        // Verify updated user data
        User returnedUser = response.as(User.class);
        Assert.assertEquals(returnedUser.getId().intValue(), userId, "User ID should remain the same");
        Assert.assertEquals(returnedUser.getName(), updatedUser.getName(), "User name should be updated");
        Assert.assertEquals(returnedUser.getEmail(), updatedUser.getEmail(), "User email should be updated");
        
        extentTest.pass("Successfully verified PUT user update");
    }

    // ==================== PATCH TESTS ====================
    
    /**
     * Test to update a post partially
     */
    @Test(groups = {"regression", "api", "patch"})
    public void testPartialUpdatePost() {
        // Test data
        int postId = 1;
        
        // Create a map with only the fields to update
        Map<String, String> updates = Map.of(
                "title", "Updated Title via PATCH"
        );
        
        // Execute request
        Response response = apiClient.patch(getEndpointUrl("posts") + "/" + postId, updates);
        
        // Log response
        logResponseToReport(response);
        
        // Validate response
        validateSuccessResponse(response, 200);
        
        // Verify only the title was updated
        Post updatedPost = response.as(Post.class);
        Assert.assertEquals(updatedPost.getId().intValue(), postId, "Post ID should remain the same");
        Assert.assertEquals(updatedPost.getTitle(), "Updated Title via PATCH", "Post title should be updated");
        Assert.assertNotNull(updatedPost.getBody(), "Post body should still exist");
        
        extentTest.pass("Successfully verified PATCH post update");
    }

    // ==================== DELETE TESTS ====================
    
    /**
     * Test to delete a user
     */
    @Test(groups = {"critical", "api", "delete"})
    public void testDeleteUser() {
        // Test data
        int userId = 1;
        
        // Execute request
        Response response = apiClient.delete(getEndpointUrl("users") + "/" + userId);
        
        // Log response
        logResponseToReport(response);
        
        // JSONPlaceholder returns 200 for DELETE with empty body
        ResponseValidator.validateStatusCode(response, 200);
        
        // Verify empty response or empty JSON
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.trim().isEmpty() || responseBody.equals("{}"), 
                "Response body should be empty or an empty JSON object");
        
        extentTest.pass("Successfully verified DELETE user");
    }
    
    /**
     * Test to delete a non-existent resource
     */
    @Test(groups = {"negative", "api", "delete"})
    public void testDeleteNonExistentResource() {
        // Test data
        int resourceId = 9999; // Non-existent resource
        
        // Execute request
        Response response = apiClient.delete(getEndpointUrl("posts") + "/" + resourceId);
        
        // Log response
        logResponseToReport(response);
        
        // JSONPlaceholder still returns 200 for DELETE of non-existent resources
        // In a real API, this might be 404 or 410
        ResponseValidator.validateStatusCode(response, 200);
        
        extentTest.pass("Successfully verified DELETE non-existent resource");
    }

    // ==================== COMPLEX SCENARIOS ====================
    
    /**
     * Test to create a post and then get its comments
     */
    @Test(groups = {"sanity", "api", "complex"})
    public void testCreatePostAndGetComments() {
        // Generate test data
        Post newPost = TestDataGenerator.generatePost(1L);
        newPost.setId(null); // Let the server assign an ID
        
        // Step 1: Create a new post
        Response createResponse = apiClient.post(getEndpointUrl("posts"), newPost);
        logResponseToReport(createResponse);
        validateSuccessResponse(createResponse, 201);
        
        // Get the created post ID
        Post createdPost = createResponse.as(Post.class);
        Long postId = createdPost.getId();
        Assert.assertNotNull(postId, "Created post should have an ID");
        
        // Step 2: Get comments for the post
        Map<String, String> queryParams = createQueryParams("postId", postId);
        Response commentsResponse = apiClient.get(getEndpointUrl("comments"), queryParams);
        logResponseToReport(commentsResponse);
        validateSuccessResponse(commentsResponse, 200);
        
        // JSONPlaceholder might not have actual comments for newly created posts,
        // but the request itself should be valid
        extentTest.pass("Successfully verified creating a post and getting its comments");
    }
    
    /**
     * Test to get user, update user, and verify update
     */
    @Test(groups = {"regression", "api", "complex"})
    public void testGetUpdateAndVerifyUser() {
        // Test data
        int userId = 2;
        
        // Step 1: Get the current user
        Response getUserResponse = apiClient.get(getEndpointUrl("users") + "/" + userId);
        logResponseToReport(getUserResponse);
        validateSuccessResponse(getUserResponse, 200);
        
        User originalUser = getUserResponse.as(User.class);
        
        // Step 2: Update the user
        originalUser.setName("Updated Name");
        originalUser.setEmail("updated.email@example.com");
        
        Response updateResponse = apiClient.put(getEndpointUrl("users") + "/" + userId, originalUser);
        logResponseToReport(updateResponse);
        validateSuccessResponse(updateResponse, 200);
        
        // Step 3: Verify the update (get the user again)
        Response getUpdatedUserResponse = apiClient.get(getEndpointUrl("users") + "/" + userId);
        logResponseToReport(getUpdatedUserResponse);
        validateSuccessResponse(getUpdatedUserResponse, 200);
        
        User updatedUser = getUpdatedUserResponse.as(User.class);
        Assert.assertEquals(updatedUser.getName(), "Updated Name", "User name should be updated");
        Assert.assertEquals(updatedUser.getEmail(), "updated.email@example.com", "User email should be updated");
        
        extentTest.pass("Successfully verified getting, updating, and re-fetching a user");
    }
} 