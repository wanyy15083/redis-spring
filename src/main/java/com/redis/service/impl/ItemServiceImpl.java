package com.redis.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.redis.cache.RedisCache;
import com.redis.dao.ItemDao;
import com.redis.entity.Item;
import com.redis.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by songyigui on 2016/8/10.
 */
@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String ITEMCACHENAME = "item_";

    @Autowired
    private ItemDao itemDao;
    @Autowired
    private RedisCache redisCache;

    @Override
    public List<Item> getItemList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Item> items = itemDao.selectByExample(null);
        return items;
    }

    @Override
    public Item getItem(Integer id) {
        String key = ITEMCACHENAME + String.valueOf(id);
        String value = redisCache.get(key);
        Item item = new Item();
        if (value == null) {
            item = itemDao.selectByPrimaryKey(id);
            try {
                value = MAPPER.writeValueAsString(item);
            } catch (JsonProcessingException e) {
                LOGGER.error("to json error:", e);
            }
            redisCache.set(key, value, 60 * 60 * 60);
            LOGGER.info("set cache with key:" + key);
            return item;
        } else {
            LOGGER.info("get cache with key:" + key);
        }
        try {
            return MAPPER.readValue(value, Item.class);
        } catch (IOException e) {
            LOGGER.error("from json error:", e);
        }
        return null;
    }

}
