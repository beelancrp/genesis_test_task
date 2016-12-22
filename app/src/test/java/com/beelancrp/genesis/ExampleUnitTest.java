package com.beelancrp.genesis;

import com.beelancrp.genesis.data.SearchItem;
import com.beelancrp.genesis.services.LoadListener;
import com.beelancrp.genesis.services.search.ImplSearchService;
import com.beelancrp.genesis.services.search.SearchService;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest implements LoadListener {

    List res;
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void searchServiceTest() throws InterruptedException {

        List<SearchItem> testItems = new ArrayList<>();
        testItems.add(new SearchItem("Мобильные телефоны Samsung (Самсунг, Somsung, Сомсунг ...", "http://rozetka.com.ua/mobile-phones/samsung/c80003/v012/"));
        testItems.add(new SearchItem("Смартфоны Samsung - Интернет магазин Rozetka.ua ...", "http://rozetka.com.ua/mobile-phones/c80003/filter/preset=smartfon;producer=samsung/"));
        testItems.add(new SearchItem("Rozetka.ua | Мобильный телефон Samsung Galaxy J7 J700H/DS ...", "http://rozetka.com.ua/samsung_galaxy_j7_ds_black/p3818722/"));
        testItems.add(new SearchItem("Rozetka.ua | Мобильный телефон Samsung Galaxy A5 2016 Duos ...", "http://rozetka.com.ua/samsung_galaxy_a5_2016_duos_sm_a510_16gb_black/p6732496/"));

        SearchService testService = new ImplSearchService(this);

        ((ImplSearchService) testService).setSizeOfOnePage(2);
        testService.search("samsung");

        Thread.sleep(500);

        assertNotNull(res);

        assertEquals(testItems, res);

    }

    @Override
    public void dataLoaded(List result) {
        res = result;
    }

    @Override
    public void dataLoadFailed(Exception e) {

    }

    @Override
    public void noExistingData() {

    }
}
