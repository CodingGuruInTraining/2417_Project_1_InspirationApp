package com.example.hl4350hb.inspirationapp;

/**
 *
 */

public class PictureEntry {

/** TODO replace arrays with one array of these objects.
    The position in getView should still reference the
    correct index.
    The takePhoto method seems like a suitable location f
    or now.
 **/
    protected String note;
    protected String imageId;
    protected long picTime;

    public PictureEntry(String note, String imageId, long time) {
        this.note = note;
        this.imageId = imageId;
        this.picTime = time;
    }

    public String getNote() {
        return note;
    }

    public String getImageId() {
        return imageId;
    }

    public long getPicTime() {
        return picTime;
    }
}
