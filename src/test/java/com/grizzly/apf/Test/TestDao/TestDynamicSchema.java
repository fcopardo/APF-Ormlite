package com.grizzly.apf.Test.TestDao;

import com.grizzly.apf.Dao.Ormlite.DynamicDao;
import com.grizzly.apf.Ormlite.ORMSchema;
import com.grizzly.apf.Test.BaseAndroidTestClass;
import com.grizzly.apf.Test.TestEntities.PersonTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by fpardo on 12/10/14.
 */
@RunWith(RobolectricTestRunner.class)
public class TestDynamicSchema extends BaseAndroidTestClass  {

    @Test
    public void TestDynamicDao01() {

        PersonTest person = new PersonTest();
        person.setId("my social id");
        person.setName("Fco Pardo");

        try {
            DynamicDao<PersonTest, String, ORMSchema> personDao = new DynamicDao<>(PersonTest.class, String.class, ORMSchema.class);
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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
