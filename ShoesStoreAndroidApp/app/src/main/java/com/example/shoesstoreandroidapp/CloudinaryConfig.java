package com.example.shoesstoreandroidapp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    private static final String CLOUD_NAME = "dtlkcmwdm";
    private static final String API_KEY = "536931462984651";
    private static final String API_SECRET = "behdOs0wRtdiYp8i7z_gTOCHslE";

    public static Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET
        ));
    }
}

