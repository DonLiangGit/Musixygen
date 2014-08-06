package com.donliang.musixygen;

public class Song {
	private String filename;
	private String duration;
	private String singer;
	private String title;
	private byte[] album_art;
	
	public String getFilenmae() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public byte[] getAlbumArt() {
		return album_art;
	}
	public void setAlbumArt(byte[] album_art) {
		this.album_art = album_art;
	}
	
}
