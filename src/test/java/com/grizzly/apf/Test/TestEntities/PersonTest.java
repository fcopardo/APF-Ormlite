package com.grizzly.apf.Test.TestEntities;

import com.grizzly.apf.Ormlite.Model.BaseModel;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Fco on 10-08-2014.
 */
public class PersonTest extends BaseModel<String> {

    @DatabaseField(columnName = "id", id = true)
    private String id;

    @DatabaseField(columnName = "name")
    private String name;

    public PersonTest(){
        super(String.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
