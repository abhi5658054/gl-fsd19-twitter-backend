package com.greatlearning.fsd.twitterapp.service;

import java.util.Set;


import com.greatlearning.fsd.twitterapp.exception.InvalidUserException;
import com.greatlearning.fsd.twitterapp.model.Tweet;
import com.greatlearning.fsd.twitterapp.model.User;

public interface UserService {
	User createUser(String userHandle,String firstName, String lastName, String emailAddress, String password);
	User updateUser(int userId, String password, String firstName, String lastName);
	void postTweet(int userId, Tweet tweet);
	void removeTweet(User user, int tweetId);
	void likeTweet(int userId, int tweetId);
	void commentOnTweet(int userId, int tweetId, String comment);
	void follow(User user, int userId);
	void unFollow(User user, int userId);
	User authenticateUser(String username, String password) throws InvalidUserException;
	Set<User> suggestUsers(int userId);
	Set<Tweet> getAllTweetsByUser(int userId);
	Set<Tweet> getAllTweetsByFollowing(int userId);
	void showFollowings(int userId);
	void showFollowers(int userId);
}
