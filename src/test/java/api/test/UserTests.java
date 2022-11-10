package api.test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	
	// faker will generate random data
	
	Faker faker;
	User userPayload;
	
	@BeforeClass
	public void SetupData()
	{
		faker= new Faker();
		userPayload =new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
	}
	
	@Test(priority=1)
	public void testPostUser()
	{
		Response response = UserEndPoints.CreateUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority=2)
	public void testGetUserByName()
	{
		Response response = UserEndPoints.readUser(this.userPayload.getUsername());
        response.then().log().all();
        response.statusCode(); // Can also get the response code
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority=3)
	public void testUpdateUserByName()
	{
		//Update data using payload
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		
		Response response = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().all();
		response.then().log().body();
		
		//Also status code can be valuated using rest assured assertion
		
		response.then().log().body().statusCode(200);
		
		Assert.assertEquals(response.getStatusCode(), 200);  // this is testng assertion
		
		//Checking data after update
		Response responseAfterUpdate = UserEndPoints.CreateUser(userPayload);
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
		
	}
	
	@Test(priority=4)
	public void deleteuserByname()
	{
		Response response=UserEndPoints.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	

}
