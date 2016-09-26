package com.redis.service;

import com.redis.entity.Item;

import java.util.List;

/**
 * Created by songyigui on 2016/8/10.
 */
public interface ItemService {

    public List<Item> getItemList(int pageNum, int pageSize);

    public Item getItem(Integer id);
}
