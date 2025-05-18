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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * ApiTestRunner - Runner class for API tests only
 * Does not initialize any browser, only runs REST API tests
 */
public class ApiTestRunner extends BaseApiTest {

    @BeforeClass
    public void setupApiTests() {
        // Set system property to skip browser initialization
        System.setProperty("skipBrowser", "true");
        LOGGER.info("API test runner initialized, browser initialization disabled");
    }

    /**
     * Test to get all users - Simple API GET test
     */
    @Test(groups = {"smoke", "api"})
    public void testGetAllUsers() {
        extentTest.info("Making GET request to /users endpoint");
        Response response = apiClient.get(getEndpointUrl("users"));
        logResponseToReport(response);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, 200);
        
        // Extract users from response
        List<User> users = response.jsonPath().getList("", User.class);
        Assert.assertFalse(users.isEmpty(), "Users list should not be empty");
        
        extentTest.pass("Successfully retrieved users from the API");
    }
    
    /**
     * Test to create a new post - API POST test
     */
    @Test(groups = {"sanity", "api"})
    public void testCreatePost() {
        // Create test data for post
        Post newPost = Post.builder()
            .title("Test Post Title")
            .body("This is a test post body created by API test automation")
            .userId(1L)
            .build();
        
        extentTest.info("Making POST request to create a new post");
        Response response = apiClient.post(getEndpointUrl("posts"), newPost);
        logResponseToReport(response);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, 201);
        
        // Verify post was created with correct data
        Post createdPost = response.as(Post.class);
        Assert.assertNotNull(createdPost.getId(), "Created post should have an ID");
        Assert.assertEquals(createdPost.getTitle(), newPost.getTitle(), "Post title should match");
        Assert.assertEquals(createdPost.getBody(), newPost.getBody(), "Post body should match");
        
        extentTest.pass("Successfully created a post via API");
    }
    
    /**
     * Test to update a post - API PUT test
     */
    @Test(groups = {"regression", "api"})
    public void testUpdatePost() {
        int postId = 1;
        Post updatedPost = Post.builder()
            .id((long) postId)
            .title("Updated Post Title")
            .body("This is an updated post body")
            .userId(1L)
            .build();
        
        extentTest.info("Making PUT request to update post with ID: " + postId);
        Response response = apiClient.put(getEndpointUrl("posts") + "/" + postId, updatedPost);
        logResponseToReport(response);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, 200);
        
        // Verify post was updated
        Post returnedPost = response.as(Post.class);
        Assert.assertEquals(returnedPost.getId().intValue(), postId, "Post ID should match");
        Assert.assertEquals(returnedPost.getTitle(), updatedPost.getTitle(), "Post title should be updated");
        Assert.assertEquals(returnedPost.getBody(), updatedPost.getBody(), "Post body should be updated");
        
        extentTest.pass("Successfully updated a post via API");
    }
    
    /**
     * Test to partially update a post - API PATCH test
     */
    @Test(groups = {"regression", "api"})
    public void testPartialUpdatePost() {
        int postId = 1;
        Map<String, String> partialUpdate = Map.of("title", "Partially Updated Title");
        
        extentTest.info("Making PATCH request to partially update post with ID: " + postId);
        Response response = apiClient.patch(getEndpointUrl("posts") + "/" + postId, partialUpdate);
        logResponseToReport(response);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, 200);
        
        // Verify post was partially updated
        Post returnedPost = response.as(Post.class);
        Assert.assertEquals(returnedPost.getId().intValue(), postId, "Post ID should match");
        Assert.assertEquals(returnedPost.getTitle(), "Partially Updated Title", "Post title should be updated");
        
        extentTest.pass("Successfully partially updated a post via API");
    }
    
    /**
     * Test to delete a post - API DELETE test
     */
    @Test(groups = {"regression", "api"})
    public void testDeletePost() {
        int postId = 1;
        
        extentTest.info("Making DELETE request for post with ID: " + postId);
        Response response = apiClient.delete(getEndpointUrl("posts") + "/" + postId);
        logResponseToReport(response);
        
        // Validate response
        ResponseValidator.validateStatusCode(response, 200);
        
        extentTest.pass("Successfully deleted a post via API");
    }
    
    /**
     * Test for a 404 response - API negative test case
     */
    @Test(groups = {"negative", "api"})
    public void testGetNonExistentResource() {
        int nonExistentId = 99999;
        
        extentTest.info("Making GET request for non-existent resource with ID: " + nonExistentId);
        Response response = apiClient.get(getEndpointUrl("users") + "/" + nonExistentId);
        logResponseToReport(response);
        
        // Validate 404 response
        ResponseValidator.validateStatusCode(response, 404);
        
        extentTest.pass("Successfully verified 404 response for non-existent resource");
    }
} 