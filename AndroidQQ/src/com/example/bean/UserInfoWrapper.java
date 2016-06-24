package com.example.bean;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class UserInfoWrapper {

    public List<UserInfo> userInfo;

    public static class UserInfo {
        //头像
        private String Head;
        //名字
        private String name;
        //签名
        private String signature;
    
		public String getHead() {
            return Head;
        }

        public void setHead(String head) {
            this.Head = head;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        @Override
        public String toString() {
            return "UserInfo [Head=" + Head + ", name=" + name + ", signature="
                    + signature + "]";
        }
    }
    
}