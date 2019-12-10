package com.greatlearning.fsd.twitterapp.service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import com.greatlearning.fsd.twitterapp.exception.InvalidUserException;
import com.greatlearning.fsd.twitterapp.model.Tweet;
import com.greatlearning.fsd.twitterapp.model.User;

public class DBUserServiceImpl implements UserService, AutoCloseable {
	private Connection connection;

	public DBUserServiceImpl() {
		InitializeDB();
	}

	private void InitializeDB() {
		Optional<Connection> optionalConnection;
		try {
			optionalConnection = JDBCUtil.getConnection();
			if(optionalConnection.isPresent()) {
				connection = optionalConnection.get();
			}
		} catch (SQLException e) {
			System.out.println("Connection error !!!");
			e.printStackTrace();
		}
	}

	@Override
	public User createUser(String userHandle,String firstName, String lastName, String emailAddress, String password) {
		User user = new User(userHandle, firstName, lastName,  emailAddress, password);
		String query = "insert into user (id, user_handle, password, first_name, last_name, email_address, profile_pic, cover_pic) "
							+ "value (?, ?, ?, ?, ?, ?, ?, ?)";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			
			preparedStatement.setInt(1, user.getUserId());
			preparedStatement.setString(2, user.getUserHandle());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getFirstName());
			preparedStatement.setString(5, user.getLastName());
			preparedStatement.setString(6, user.getEmailAddress());
			preparedStatement.setString(7, user.getProfilePic());
			preparedStatement.setString(8, user.getCoverPic());
			
			executeCommand(preparedStatement, query, Operation.INSERT);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public User updateUser(int userId, String password, String firstName, String lastName) {
		User user = validateUserId(userId);
		
		String query = "update user set password = ?, first_name = ?, last_name = ? where id = ?";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, password);
			preparedStatement.setString(2, firstName);
			preparedStatement.setString(3, lastName);
			preparedStatement.setInt(4,  userId);
			
			executeCommand(preparedStatement, query, Operation.UPDATE);
			user = fetchUserById(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public void postTweet(int userId, Tweet tweet) {
		User user =	validateUserId(userId);
		
		String query = "insert into tweet (id, user_id, tweet_message, likes) value (?, ?, ?, ?)";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, tweet.getId());
			preparedStatement.setInt(2, userId);
			preparedStatement.setString(3, tweet.getTweetMsg());
			preparedStatement.setInt(4, tweet.getLikes());
			
			executeCommand(preparedStatement, query, Operation.INSERT);
			user.addTweet(tweet);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeTweet(User user, int tweetId) {
		Tweet tweet = fetchTweetById(tweetId);
		
		if(tweet == null)
			throw new IllegalArgumentException("Invalid Tweet Id !!!");
		
		String query = "delete from tweet where id = ?";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, tweetId);

			executeCommand(preparedStatement, query, Operation.DELETE);
			user.deleteTweet(tweet);
		} catch(SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void likeTweet(int userId, int tweetId) {
		Tweet tweet = fetchTweetById(tweetId);
		
		if(tweet == null)
			throw new IllegalArgumentException("Invalid Tweet Id !!!");
		
		int likeCount = tweet.getLikes() + 1;
		String query = "update tweet set likes = " + likeCount + " where id = ?";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, tweetId);
			
			executeCommand(preparedStatement, query, Operation.UPDATE);
			tweet.setLikes(likeCount);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void commentOnTweet(int userId, int tweetId, String comment) {
		Tweet tweet = fetchTweetById(tweetId);
		
		if(tweet == null)
			throw new IllegalArgumentException("Invalid Tweet Id !!!");
		
		String query = "insert into comment (tweet_id, user_id, text) value (?, ?, ?)";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, tweetId);
			preparedStatement.setInt(2, userId);
			preparedStatement.setString(3, comment);
			
			executeCommand(preparedStatement, query, Operation.INSERT);
			tweet.addComment(comment);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void follow(User user, int userId) {
		User following = fetchUserById(userId);
		
		if(following == null)
			throw new IllegalArgumentException("Not a valid user !!!");
		if(following.equals(user) == true)
			throw new IllegalArgumentException("Cannot follow self !!!");
		
		String query = "insert into follower_following (follower_id, following_id) value (?, ?)";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, user.getUserId());
			preparedStatement.setInt(2, userId);
			
			executeCommand(preparedStatement, query, Operation.INSERT);
			following.addFollower(user);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void unFollow(User user, int userId) {
		User following = fetchUserById(userId);
		
		if(following == null)
			throw new IllegalArgumentException("Not a valid user !!!");
		if(following.equals(user) == true)
			throw new IllegalArgumentException("Cannot follow self !!!");
		
		String query = "delete from follower_following where follower_id = ? and following_id = ?";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, user.getUserId());
			preparedStatement.setInt(2, following.getUserId());
			
			executeCommand(preparedStatement, query, Operation.INSERT);
		    following.removeFolllower(user);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User authenticateUser(String userHandle, String password) throws InvalidUserException {
		User user = null;
		String query = "select * from user where user_handle = ? and password = ?";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, userHandle);
			preparedStatement.setString(2, password);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				int userId = rs.getInt(1);
				String firstName = rs.getString(4);
				String lastName = rs.getString(5);
				String emailAddress = rs.getString(6);
				String profilePic = rs.getString(7);
				String cover_pic = rs.getString(8);
				
				user = new User(userId, userHandle, password, firstName, lastName, emailAddress, profilePic, cover_pic);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(user == null)
			throw new InvalidUserException("Username/Password donot match !!!");
		
		return user;
	}

	@Override
	public Set<User> suggestUsers(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Tweet> getAllTweetsByUser(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Tweet> getAllTweetsByFollowing(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showFollowings(int userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showFollowers(int userId) {
		// TODO Auto-generated method stub
		
	}
	
	private User validateUserId(int userId) {
		User user = fetchUserById(userId);
		if(user == null) 
			throw new IllegalArgumentException("Invalid User Id");
		
		return user;
	}

	private User fetchUserById(int userId) {
		User user = null;
		String query = "select * from user where id = ?";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userId);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				String userHandle = rs.getString(2);
				String password = rs.getString(3);
				String firstName = rs.getString(4);
				String lastName = rs.getString(5);
				String emailAddress = rs.getString(6);
				String profilePic = rs.getString(7);
				String cover_pic = rs.getString(8);
				
				//TODO: Persist the tweets of the user from DB to user's Set<Tweet> tweets
				user = new User(userId, userHandle, password, firstName, lastName, emailAddress, profilePic, cover_pic);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}
	
	private Tweet fetchTweetById(int tweetId) {
		Tweet tweet = null;
		String query = "select * from tweet where id = ?";
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, tweetId);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				String tweetMsg = rs.getString(3);
				int userId = rs.getInt(2);
				User user = fetchUserById(userId);
				int likes = rs.getInt(5);
				
				tweet = new Tweet(user, tweetMsg, tweetId);
				tweet.setLikes(likes);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tweet;
	}
	private void executeCommand(PreparedStatement preparedStatement, String query, Operation operation) throws SQLException {
		switch (operation) {
		case INSERT:
		case DELETE:
		case UPDATE:
			int records = preparedStatement.executeUpdate();
			if(records > 0)
				System.out.println("Rows effected: " + records);
			break;
		case QUERY:
			ResultSet rs = preparedStatement.executeQuery(query);
			while (rs.next()) {
                
				System.out.print("Id: " + rs.getInt(1) + " ");
				System.out.print("UserHandle: " + rs.getString(2) + " ");
				System.out.print("First Name: " + rs.getString(4) + " ");
				System.out.println();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void close() {
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
