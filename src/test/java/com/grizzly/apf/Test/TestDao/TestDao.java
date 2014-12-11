package com.grizzly.apf.Test.TestDao;

import android.database.sqlite.SQLiteDatabase;
import com.grizzly.apf.Dao.Ormlite.AdvancedDao;
import com.grizzly.apf.Dao.Ormlite.DaoFactory;
import com.grizzly.apf.Exceptions.GrizzlyModelException;
import com.grizzly.apf.Ormlite.CallBacks.OnSchemaCreation;
import com.grizzly.apf.Ormlite.ORMSchema;
import com.grizzly.apf.Test.BaseAndroidTestClass;
import com.grizzly.apf.Test.TestEntities.PersonTest;
import com.grizzly.apf.Test.TestSchemas.TestSchema;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.sql.SQLException;

/**
 * Created by Fco on 10-08-2014.
 */
@RunWith(RobolectricTestRunner.class)
public class TestDao extends BaseAndroidTestClass {

    @Test
    public void TestDao01() {

        DaoFactory<TestSchema> daoFactory = new DaoFactory<>(TestSchema.class);

        PersonTest person = new PersonTest();
        person.setId("my social id");
        person.setName("Fco Pardo");

        try {
            daoFactory.getProperDao(PersonTest.class, getContext()).setSource(person);
            org.junit.Assert.assertEquals("Failure: creation", true, daoFactory.getProperDao(PersonTest.class, getContext()).create());
            org.junit.Assert.assertEquals("Failure: creation", false, daoFactory.getProperDao(PersonTest.class, getContext()).create(person));

            PersonTest person2 = daoFactory.getProperDao(PersonTest.class, getContext()).getSource();

            System.out.println("\n My person is:"+person2.getId());
            person2.setName("a new name");
            daoFactory.getProperDao(PersonTest.class, getContext()).setSource(person2);

            org.junit.Assert.assertEquals("Failure: updating", true, daoFactory.getProperDao(PersonTest.class, getContext()).update());
            daoFactory.getProperDao(PersonTest.class, getContext()).setSource(person);

            System.out.println("My ID is: "+daoFactory.getProperDao(PersonTest.class, getContext()).getSource().getId());

            org.junit.Assert.assertEquals("Failure: search", true, daoFactory.getProperDao(PersonTest.class, getContext()).find());
            System.out.println("\nMy inner result is: "+daoFactory.getProperDao(PersonTest.class, getContext()).getSource().getId());

            org.junit.Assert.assertEquals("Failure: search", true, daoFactory.getProperDao(PersonTest.class, getContext()).find("my social id u"));
            boolean bol = false;
            try {
                daoFactory.getProperDao(PersonTest.class, getContext()).setSource(daoFactory.getProperDao(PersonTest.class, getContext()).getMyDao().queryForId(person2.getId()));
                bol = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            org.junit.Assert.assertEquals("Failure: search", true, bol);

            System.out.println("\nMy outer result is: "+daoFactory.getProperDao(PersonTest.class, getContext()).getSource().getId());

        } catch (GrizzlyModelException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void TestDao02() {

        DaoFactory<TestSchema> daoFactory = new DaoFactory<>(TestSchema.class);


        PersonTest person = new PersonTest();
        person.setId("my social id");
        person.setName("Fco Pardo");

        try {
            AdvancedDao<PersonTest, String, TestSchema> personDao = daoFactory.getProperDao(PersonTest.class, getContext());
            personDao.setSource(person);
            org.junit.Assert.assertEquals("Failure: creation", true, personDao.create());
            org.junit.Assert.assertEquals("Failure: creation", false, personDao.create(person));

            PersonTest person2 = personDao.getSource();

            System.out.println("\n My person is:"+person2.getId());
            person2.setName("a new name");
            personDao.setSource(person2);

            org.junit.Assert.assertEquals("Failure: updating", true, personDao.update());
            personDao.setSource(person);

            System.out.println("My ID is: "+personDao.getSource().getId());

            org.junit.Assert.assertEquals("Failure: search", true, personDao.find());
            System.out.println("\nMy inner result is: "+personDao.getSource().getId());

            org.junit.Assert.assertEquals("Failure: search", true, personDao.find("my social id"));
            System.out.println("\nMy outer result is: "+personDao.getSource().getId());


        } catch (GrizzlyModelException e) {
            e.printStackTrace();
        }
    }

}

