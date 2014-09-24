package com.liulei1947.bt.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class QiangYu extends BmobObject implements Serializable{

	/**
	 * qiang yu entity,每个列表item内容
	 * 2014/4/27
	 */
	private static final long serialVersionUID = -6280656428527540320L;
	
	private User author;
	private String FilmName;
	private String yunpanUrl;
	private BmobFile indexImageUrl;
	private BmobFile uTorrentFileUrl;
	private int love;
	private int hate;
	private int share;
	private int comment;
	private boolean isPass;
	private boolean isHeaderShow;
	private boolean myFav;//收藏
	private boolean myLove;//赞
	private BmobRelation relation;

	public BmobRelation getRelation() {
		return relation;
	}
	public void setRelation(BmobRelation relation) {
		this.relation = relation;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public String getFilmName() {
		return FilmName;
	}
	public void setFilmName(String FilmName) {
		this.FilmName = FilmName;
	}
	public String getyunpanUrl() {
		return yunpanUrl;
	}
	public void setyunpanUrl(String yunpanUrl) {
		this.yunpanUrl = yunpanUrl;
	}
	public BmobFile getuTorrentFile() {
		return uTorrentFileUrl;
	}
	public void setuTorrentFile(BmobFile uTorrentFileUrl) {
		this.uTorrentFileUrl = uTorrentFileUrl;
	}
	public BmobFile getindexImageUrl() {
		return indexImageUrl;
	}
	public void setindexImageUrl(BmobFile indexImageUrl) {
		this.indexImageUrl = indexImageUrl;
	}
	public int getLove() {
		return love;
	}
	public void setLove(int love) {
		this.love = love;
	}
	public int getHate() {
		return hate;
	}
	public void setHate(int hate) {
		this.hate = hate;
	}
	public int getShare() {
		return share;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public boolean isPass() {
		return isPass;
	}
	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}
	public boolean isHeaderShow() {
		return isHeaderShow;
	}
	public void setisHeaderShow(boolean isHeaderShow) {
		this.isHeaderShow = isHeaderShow;
	}
	public boolean getMyFav() {
		return myFav;
	}
	public void setMyFav(boolean myFav) {
		this.myFav = myFav;
	}
	public boolean getMyLove() {
		return myLove;
	}
	public void setMyLove(boolean myLove) {
		this.myLove = myLove;
	}
	@Override
	public String toString() {
		return "QiangYu [author=" + author + ", FilmName=" + FilmName
				+ ", FilmName=" + indexImageUrl + ", love=" + love
				+ ", hate=" + hate + ", share=" + share + ", comment="
				+ comment + ", isPass=" + isPass + ", myFav=" + myFav
				+ ", myLove=" + myLove + ", relation=" + relation + "]";
	}
	
}
