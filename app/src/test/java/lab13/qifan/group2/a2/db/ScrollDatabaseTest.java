package lab13.qifan.group2.a2.db;

import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class ScrollDatabaseTest {
    private ScrollDatabase scrollDatabase;
    DigitalScroll scroll;
    @BeforeEach
    public void setUp()
    {
        scroll = new DigitalScroll("test","test","test","testID",0);

        scrollDatabase = new ScrollDatabase();
    }
    @AfterEach
    public void cleanUp()
    {

        scrollDatabase.deleteScroll(scroll);
    }

    @Test
    @Order(1)
    public void testAddAndRemoveScroll()
    {
        scrollDatabase.addScroll(scroll);
        DigitalScroll getScroll = scrollDatabase.getScroll("test");
        assertNotNull(getScroll);
    }
    @Test
    @Order(2)
    public void testUpdateScroll()
    {
        scrollDatabase.addScroll(scroll);
        DigitalScroll newScroll = new DigitalScroll("testUpdate","testUpdate","testUpdate","testIDUpdate",10);
        scrollDatabase.updateScroll(newScroll,scroll.getId());
        DigitalScroll grabScroll = scrollDatabase.getScroll("testUpdate");
        assertEquals("testUpdate",grabScroll.getId());
        assertEquals("testUpdate",grabScroll.getName());
        assertEquals("testUpdate",grabScroll.getFilePath());
        assertEquals("testIDUpdate",grabScroll.getUserIdKey());
        assertEquals(10,grabScroll.getDownloadCount());
        scrollDatabase.deleteScroll(newScroll);

    }

    @Test
    @Order(4)
    public void testGetArrayScroll()
    {
        DigitalScroll scroll1 = new DigitalScroll("test1","test1","test1","testID1",1);
        scrollDatabase.addScroll(scroll1);
        DigitalScroll scroll2 = new DigitalScroll("test2","test2","test2","testID2",2);
        scrollDatabase.addScroll(scroll2);
        DigitalScroll scroll3 = new DigitalScroll("test3","test3","test3","testID3",3);
        scrollDatabase.addScroll(scroll3);

        ArrayList<DigitalScroll> array = scrollDatabase.getArrayScrolls();

        boolean found1 = false;
        for(DigitalScroll scroll: array)
        {
            if(scroll.getId().equals(scroll1.getId()))
            {
                found1 = true;
            }
        }
        boolean found2 = false;
        for(DigitalScroll scroll: array)
        {
            if(scroll.getId().equals(scroll2.getId()))
            {
                found2 = true;
            }
        }
        boolean found3 = false;
        for(DigitalScroll scroll: array) {
            if (scroll.getId().equals(scroll3.getId())) {
                found3 = true;
            }
        }
        assertTrue(found1);
        assertTrue(found2);
        assertTrue(found3);

        scrollDatabase.deleteScroll(scroll1);
        scrollDatabase.deleteScroll(scroll2);
        scrollDatabase.deleteScroll(scroll3);
    }
}
