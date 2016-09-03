package com.tms.model.datatype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

/**
 * Created by yzheng on 8/31/16.
 */

@AllArgsConstructor
public class ContactInfo {

    enum ContactType {

        WECHAT("wechat"),
        QQ("qq"),
        PHONE("phone"),
        EMAIL("email");

        private final String contactType;

        private ContactType(String contactType) {
            this.contactType = contactType;
        }

        public static ContactType toContactMethod(String contactMethod) {
            if (contactMethod == null) {    return null;    }
            for (ContactType ct : values()) {
                if (ct.toString().equals(contactMethod)) {
                    return ct;
                }
            }

            throw new IllegalArgumentException("Unsupported contact method = " + contactMethod);
        }

        public String toString() {  return this.contactType; }
    }

    @NotNull
    private ContactType contactType;
    @NotNull
    private  String detail;

    @JsonProperty("method")
    public void setContactType(String type) {
        System.out.println(type);
        this.setContactType(ContactType.toContactMethod(type));
    }

    @JsonProperty("method")
    public String getContactMethod() {
        return this.contactType.toString();
    }

    public String getDetail() { return detail; }

    public void setDetail(String detail) { this.detail = detail; }

    @JsonIgnore
    public ContactType getContactType() { return contactType; }

    @JsonIgnore
    public void setContactType(ContactType contactType) { this.contactType = contactType; }

    /*
    public static void main(String[] args) {
        ContactInfo contactInfo = new ContactInfo(ContactType.WECHAT, "averillzheng");
        System.out.println(ObjectMapperSingleton.getInstance().convertValue(contactInfo, Map.class));
        Map<String, String> contact = new HashMap<String, String>(){
            {
                put("method", "PHONE");
                put("detail", "12345678");
            }
        };

        ContactInfo contactInfo1 = ObjectMapperSingleton.getInstance().convertValue(contact, ContactInfo.class);
        System.out.println(contactInfo1.contactType == ContactType.PHONE);
        System.out.println(contactInfo1.getDetail());

    }*/
}
