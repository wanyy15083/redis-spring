package com.redis.test;

import com.redis.dao.ItemDao;
import com.redis.entity.Item;
import com.redis.entity.NutritionFacts;
import com.redis.service.ItemService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.mockito.Mockito.when;

/**
 * 测试基类（无事务回滚）
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-*.xml"})
public class BaseTestCase extends AbstractJUnit4SpringContextTests {

    @InjectMocks
    @Autowired
    private ItemService itemService;

    @Mock
    private ItemDao itemDao;

    Item item = new Item(1,"篮球");

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(itemDao.selectByPrimaryKey(1)).thenReturn(item);
    }

    @Test
    public void testItemService() {
//        List<Item> itemList = itemService.getItemList(0, 10);
        Item i = itemService.getItem(1);
        System.out.println(i.toString());
        Assert.assertSame(i, item);
    }


    @Test
    public void others(){
//        NutritionFacts coca = new NutritionFacts.Builder(240,8).calories(100).sodium(20).calories(2).carbohydrate(30).build();
        Item item = itemService.getItem(50);
        System.out.println(item);

    }

}