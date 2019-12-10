package com.greatlearning.fsd.twitterapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public final class Tweet implements Comparable<Tweet> {
	public static int tweetIdCounter = 100;
	
	private final int id;
	private final User user;
	private String tweetMsg;
	private List<Media> mediaUrls;

	private final Date createdDate = new Date();
	private int likes;
	private final List<String> comments = new ArrayList<>();
	private int commentsCount;

	public Tweet(User user, String tweetMsg, List<Media> mediaUrls) {		
		this.id = ++ tweetIdCounter;
		this.user = user;
		this.tweetMsg = tweetMsg;
		this.mediaUrls = mediaUrls;
	}
	public Tweet(User user, String tweetMsg) {		
		this.id = ++ tweetIdCounter;
		this.user = user;
		this.tweetMsg = tweetMsg;
	}
	public Tweet(User user, String tweetMsg, int id) {		
		this.id = id;
		this.user = user;
		this.tweetMsg = tweetMsg;
	}
	
	public int getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}

	public String getTweetMsg() {
		return tweetMsg;
	}
	public void setTweetMsg(String tweetMsg) {
		this.tweetMsg = tweetMsg;
	}
	
	public Date getCreatedDt() {
		return createdDate;
	}

	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public void addLike() {
		this.likes++;
	}
	
	public List<String> getComments() {
		return comments;
	}
	public void addComment(String comment) {
		this.comments.add(comment);
		++commentsCount;
	}
	public int getCommentsCount() {
		return commentsCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tweet other = (Tweet) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (id != other.id)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "\n{" + "Tweet Id: " + id + "\n" + 
				"Tweet:" + tweetMsg + "\n" +
				"By: " + user.getFirstName() + "\n" + 
				"createdDt: " + createdDate + "\n" +
				"Likes: " + likes + "\n" +
				"Comments: " + comments + "}\n";
	}
	
	@Override
	public int compareTo(Tweet tweet) {
		return this.tweetMsg.compareTo(tweet.tweetMsg);
	}
	
	public enum MediaType{
		AUDIO,
		VIDEO,
		IMAGE
	}

	public class Media{
		private MediaType mediaType;
		private String url;
		
		public Media(MediaType mediaType, String url) {
			this.mediaType = mediaType;
			this.url = url;
		}
	}
}
