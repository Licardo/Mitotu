package com.miaotu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Jayden on 2015/5/21.
 */
public class PersonInfo implements Serializable {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Phone")
    private String phone;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("Uid")
    private String uid;
    @JsonProperty("Token")
    private String token;
    @JsonProperty("Nickname")
    private String nickname;
    @JsonProperty("Status")
    private String status;

    @JsonProperty("Code")
    private String usercode;

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getMd5uid() {
        return md5uid;
    }

    public void setMd5uid(String md5uid) {
        this.md5uid = md5uid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getWxunionid() {
        return wxunionid;
    }

    public void setWxunionid(String wxunionid) {
        this.wxunionid = wxunionid;
    }

    public String getQqopenid() {
        return qqopenid;
    }

    public void setQqopenid(String qqopenid) {
        this.qqopenid = qqopenid;
    }

    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("Birthday")
    private String birthday;
    @JsonProperty("Country")
    private String country;
    @JsonProperty("Province")
    private String province;
    @JsonProperty("City")
    private String city;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Age")
    private String age;
    @JsonProperty("Constellation")
    private String constellation;
    @JsonProperty("Md5Uid")
    private String md5uid;
    @JsonProperty("CreateTime")
    private String createtime;
    @JsonProperty("WxUnionid")
    private String wxunionid;
    @JsonProperty("QqOpenid")
    private String qqopenid;

    @JsonProperty("SinaUid")
    private String sinauid;
    @JsonProperty("HeadUrl")
    private String headurl;
    @JsonProperty("AboutMe")
    private String aboutme;
    @JsonProperty("High")
    private String high;
    @JsonProperty("Education")
    private String education;
    @JsonProperty("MaritalStatus")
    private String maritalstatus;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("GraduateSchool")
    private String graduateschool;
    @JsonProperty("Work")
    private String work;
    @JsonProperty("Tags")
    private String tags;
    @JsonProperty("BodyType")
    private String bodytype;
    @JsonProperty("WantGo")
    private String wantgo;
    @JsonProperty("BeenGo")
    private String beengo;
    @JsonProperty("Hobbies")
    private String hobbies;
    @JsonProperty("Film")
    private String film;
    @JsonProperty("Music")
    private String music;
    @JsonProperty("Book")
    private String book;
    @JsonProperty("Food")
    private String food;
    @JsonProperty("LikeCount")
    private String likecount;
    @JsonProperty("CollectCount")
    private String collectcount;
    @JsonProperty("JoinCount")
    private String joincount;
    @JsonProperty("PhotoCount")
    private String photocount;

    public String getSinauid() {
        return sinauid;
    }

    public void setSinauid(String sinauid) {
        this.sinauid = sinauid;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
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

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGraduateschool() {
        return graduateschool;
    }

    public void setGraduateschool(String graduateschool) {
        this.graduateschool = graduateschool;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBodytype() {
        return bodytype;
    }

    public void setBodytype(String bodytype) {
        this.bodytype = bodytype;
    }

    public String getWantgo() {
        return wantgo;
    }

    public void setWantgo(String wantgo) {
        this.wantgo = wantgo;
    }

    public String getBeengo() {
        return beengo;
    }

    public void setBeengo(String beengo) {
        this.beengo = beengo;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
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

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    public String getCollectcount() {
        return collectcount;
    }

    public void setCollectcount(String collectcount) {
        this.collectcount = collectcount;
    }

    public String getJoincount() {
        return joincount;
    }

    public void setJoincount(String joincount) {
        this.joincount = joincount;
    }

    public String getPhotocount() {
        return photocount;
    }

    public void setPhotocount(String photocount) {
        this.photocount = photocount;
    }
}
