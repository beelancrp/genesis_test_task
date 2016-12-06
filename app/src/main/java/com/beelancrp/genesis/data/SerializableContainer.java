package com.beelancrp.genesis.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bilan on 06.12.2016.
 */

public class SerializableContainer<E extends Object> implements Serializable {
    public static final long serialVersionUID = 1L;

    List<E> list;

    public SerializableContainer(List<E> list) {
        this.list = list;
    }

    public List<E> getList() {
        return list;
    }
}
