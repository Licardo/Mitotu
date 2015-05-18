package com.miaotu.imutil;

import com.easemob.chat.EMConversation;

public class Conversation {
private EMConversation emConversation;
private ContactInfo contactInfo;
public EMConversation getEmConversation() {
	return emConversation;
}
public void setEmConversation(EMConversation emConversation) {
	this.emConversation = emConversation;
}
public ContactInfo getContactInfo() {
	return contactInfo;
}
public void setContactInfo(ContactInfo contactInfo) {
	this.contactInfo = contactInfo;
}

}
