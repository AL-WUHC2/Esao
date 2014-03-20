package org.n3r.es.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.joda.time.DateTime;
import org.junit.Test;
import org.n3r.core.collection.RList;
import org.n3r.core.collection.RMap;
import org.n3r.es.source.from.BeanFromSource;
import org.n3r.es.source.to.BeanToSource;

public class EsBeanSourceTest {

    @Test
    public void testBeanSourceConvert() throws IOException {
        ConvertBean bean = new ConvertBean();
        bean.setField01(1);
        bean.setField02(2.3f);
        bean.setField03(true);
        bean.setField04("456");

        Date date = new DateTime("2014-03-19T21:53:01.924").toDate();
        bean.setField05(date);

        List<SubBean> list = new ArrayList<SubBean>();
        SubBean sub01 = new SubBean();
        sub01.setSub01("aaa");
        sub01.setSub02(date);
        list.add(sub01);
        SubBean sub02 = new SubBean();
        sub02.setSub01("bbb");
        sub02.setSub02(date);
        list.add(sub02);
        bean.setField06(list);

        Map<KeyBean, ValueBean> map = new HashMap<KeyBean, ValueBean>();
        KeyBean key01 = new KeyBean();
        key01.setKey("ccc");
        ValueBean value01 = new ValueBean();
        value01.setValue(3);
        map.put(key01, value01);
        KeyBean key02 = new KeyBean();
        key02.setKey("ddd");
        ValueBean value02 = new ValueBean();
        value02.setValue(4);
        map.put(key02, value02);
        bean.setField07(map);

        SubBean sub = new SubBean();
        sub.setSub01("qwert");
        sub.setSub02(date);
        bean.setField08(sub);

        Map<String, Object> source = new BeanToSource().toSource(bean);
        assertEquals(1, source.get("field01"));
        assertEquals(2.3f, source.get("field02"));
        assertEquals(true, source.get("field03"));
        assertEquals("456", source.get("field04"));
        assertEquals("2014-03-19T21:53:01.924", source.get("field05"));
        List<Map<String,String>> expList = RList.asList(RMap.of("sub01", "aaa", "sub02", "2014-03-19 21:53:01"),
                RMap.of("sub01", "bbb", "sub02", "2014-03-19 21:53:01"));
        assertEquals(expList, source.get("field06"));
        Map<Map<String, String>, Map<String, Integer>> expMap = RMap.of(
                RMap.of("key", "ccc"), RMap.of("value", 3),
                RMap.of("key", "ddd"), RMap.of("value", 4));
        assertEquals(expMap, source.get("field07"));
        Map<String, String> expSub = RMap.of("sub01", "qwert", "sub02", "2014-03-19 21:53:01");
        assertEquals(expSub, source.get("field08"));

        ConvertBean bean2 = new BeanFromSource().fromSource(source, ConvertBean.class);
        assertEquals(1, bean2.getField01());
        assertTrue(2.3f == bean2.getField02());
        assertTrue(bean2.getField03());
        assertEquals("456", bean2.getField04());
        assertEquals(date, bean2.getField05());
        assertEquals(list, bean2.getField06());
        assertEquals(map, bean2.getField07());
        assertTrue(sub.equals(bean2.getField08()));
    }

}
