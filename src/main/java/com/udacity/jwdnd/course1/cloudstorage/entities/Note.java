package com.udacity.jwdnd.course1.cloudstorage.entities;

import javax.validation.constraints.NotEmpty;

public class Note {
    private Integer noteid;
    @NotEmpty(message = "Mote Should not be empty")
    private String notetitle;
    @NotEmpty(message = "Note Description Should not be empty")
    private String notedescription;
    private Integer userid;

    public Integer getNoteid() {
        return noteid;
    }

    public void setNoteid(Integer noteid) {
        this.noteid = noteid;
    }

    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotedescription() {
        return notedescription;
    }

    public void setNotedescription(String notedescription) {
        this.notedescription = notedescription;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
