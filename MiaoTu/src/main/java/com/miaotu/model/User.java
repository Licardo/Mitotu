package com.miaotu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.annotation.FormProperty;

public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @FormProperty("uid")
    @JsonProperty("uid")
    private String id;
    @FormProperty("constellation")
    @JsonProperty("constellation")
    private String constellation;
    @FormProperty("tags")
    @JsonProperty("tags")
    private List<String> tags;
    @FormProperty("age")
    @JsonProperty("age")
    private String age;
    @FormProperty("sex")
    @JsonProperty("sex")
    private String gender;
    @FormProperty("nickname")
    @JsonProperty("nickname")
    private String nickname;
    @FormProperty("name")
    @JsonProperty("name")
    private String name;
    @FormProperty("phone")
    @JsonProperty("phone")
    private String phone;
    @FormProperty("city")
    @JsonProperty("city")
    private String city;
    @FormProperty("email")
    @JsonProperty("email")
    private String email;
    @FormProperty("token")
    @JsonProperty("token")
    private String token;
    @FormProperty("province")
    @JsonProperty("province")
    private String province;
    @FormProperty("high")
    @JsonProperty("high")
    private String high;
    @FormProperty("education")
    @JsonProperty("education")
    private String education;
    @FormProperty("graduate_school")
    @JsonProperty("graduate_school")
    private String graduated;
    @FormProperty("work")
    @JsonProperty("work")
    private String work;
    @FormProperty("about_me")
    @JsonProperty("about_me")
    private String about;
    @FormProperty("month_pay")
    @JsonProperty("month_pay")
    private String monthPay;
    @FormProperty("birthday")
    @JsonProperty("birthday")
    private String birthday;
    @FormProperty("body_type")
    @JsonProperty("body_type")
    private String bodyType;
    @FormProperty("pic_ids")
    @JsonProperty("pic_ids")
    private ArrayList<PhotoInfo> pics;
    @FormProperty("hobbies")
    @JsonProperty("hobbies")
    private String hobbies;
    @FormProperty("been_go")
    @JsonProperty("been_go")
    private String beenGo;
    @FormProperty("want_go")
    @JsonProperty("want_go")
    private String wantGo;
    @FormProperty("marital_status")
    @JsonProperty("marital_status")
    private String maritalStatus;
    @FormProperty("liked")
    @JsonProperty("liked")
    private String liked;
    @FormProperty("liked_me")
    @JsonProperty("liked_me")
    private String likedMe;
    @FormProperty("like_to_like")
    @JsonProperty("like_to_like")
    private String likeToLike;
    @FormProperty("join_count")
    @JsonProperty("join_count")
    private String joinCount;
    @FormProperty("collect_count")
    @JsonProperty("collect_count")
    private String collectCount;
    @FormProperty("avatar")
    @JsonProperty("avatar")
    private PhotoInfo avatar;
    @FormProperty("join")
    @JsonProperty("join")
    private JoinOrCollect join;
    @FormProperty("collect")
    @JsonProperty("collect")
    private JoinOrCollect collect;
    @FormProperty("film")
    @JsonProperty("film")
    private String film;
    @FormProperty("book")
    @JsonProperty("book")
    private String book;
    @FormProperty("food")
    @JsonProperty("food")
    private String food;
    @FormProperty("music")
    @JsonProperty("music")
    private String music;
    @FormProperty("is_dr")
    @JsonProperty("is_dr")
    private Integer isDr;
    @JsonProperty("online")
    private boolean isOnline;
    @JsonProperty("dr")
    private Sponsor sponsor;
    @FormProperty("dr_content")
    @JsonProperty("dr_content")
    private String drContent;
    @FormProperty("lucky_money")
    @JsonProperty("lucky_money")
    private String luckMoney;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGraduated() {
        return graduated;
    }

    public void setGraduated(String graduated) {
        this.graduated = graduated;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getMonthPay() {
        return monthPay;
    }

    public void setMonthPay(String monthPay) {
        this.monthPay = monthPay;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public ArrayList<PhotoInfo> getPics() {
        return pics;
    }

    public void setPics(ArrayList<PhotoInfo> pics) {
        this.pics = pics;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getBeenGo() {
        return beenGo;
    }

    public void setBeenGo(String beenGo) {
        this.beenGo = beenGo;
    }

    public String getWantGo() {
        return wantGo;
    }

    public void setWantGo(String wantGo) {
        this.wantGo = wantGo;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getLikedMe() {
        return likedMe;
    }

    public void setLikedMe(String likedMe) {
        this.likedMe = likedMe;
    }

    public String getLikeToLike() {
        return likeToLike;
    }

    public void setLikeToLike(String likeToLike) {
        this.likeToLike = likeToLike;
    }

    public String getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(String joinCount) {
        this.joinCount = joinCount;
    }

    public String getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }

    public JoinOrCollect getJoin() {
        return join;
    }

    public void setJoin(JoinOrCollect join) {
        this.join = join;
    }

    public JoinOrCollect getCollect() {
        return collect;
    }

    public void setCollect(JoinOrCollect collect) {
        this.collect = collect;
    }

    public PhotoInfo getAvatar() {
        return avatar;
    }

    public void setAvatar(PhotoInfo avatar) {
        this.avatar = avatar;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public Integer getIsDr() {
        return isDr;
    }

    public void setIsDr(Integer isDr) {
        this.isDr = isDr;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public String getDrContent() {
        return drContent;
    }

    public void setDrContent(String drContent) {
        this.drContent = drContent;
    }

    public String getLuckMoney() {
        return luckMoney;
    }

    public void setLuckMoney(String luckMoney) {
        this.luckMoney = luckMoney;
    }
}
