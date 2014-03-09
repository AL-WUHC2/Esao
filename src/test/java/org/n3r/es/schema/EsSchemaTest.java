package org.n3r.es.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;
import org.n3r.es.schema.builder.EsSchemaBuilder;

public class EsSchemaTest {

    @Test
    public void testSimpleBean() throws Exception {
        EsSchema schema = new EsSchemaBuilder(SimpleBean.class).schema();
        assertEquals(SimpleBean.class.getSimpleName(), schema.getIndexes()[0]);
        assertEquals(SimpleBean.class.getCanonicalName(), schema.getType());

        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject(SimpleBean.class.getCanonicalName())
                .startObject("properties")
                .startObject("simpleInteger").field("type", "integer").endObject()
                .startObject("simpleString").field("type", "string").endObject()
                .endObject()
                .endObject()
                .endObject();
        assertEquals(mapping.string(), schema.getSource());
    }

    @Test
    public void testSimpleAnnoBean() throws Exception {
        EsSchema schema = new EsSchemaBuilder(SimpleAnnoBean.class).schema();
        assertEquals("simple", schema.getIndexes()[0]);
        assertEquals("anno", schema.getIndexes()[1]);
        assertEquals("simpleType", schema.getType());

        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("simpleType")
                .startObject("_id")
                .field("index", "not_analyzed")
                .field("path", "simpleInteger")
                .field("store", true)
                .endObject()
                .startObject("properties")
                .startObject("originalDate").field("store", true).field("type", "string").endObject()
                .startObject("simpleInteger").field("index_name", "intField").field("type", "integer").endObject()
                .startObject("simpleString").field("index", "no").field("type", "string").endObject()
                .startObject("stringDate").field("format", "yyyy-MM-dd").field("type", "date").endObject()
                .endObject()
                .endObject()
                .endObject();
        assertEquals(mapping.string(), schema.getSource());
    }

    @Test
    public void testNestedBean() throws Exception {
        EsSchema schema = new EsSchemaBuilder(NestedBean.class).schema();
        assertEquals("Nested", schema.getIndexes()[0]);
        assertEquals("NestedBean", schema.getType());

        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("NestedBean")
                .startObject("_id")
                .field("path", "simpleAnno.simpleInteger")
                .endObject()
                .startObject("properties")
                .startObject("comment").field("index", "no").field("type", "string").endObject()
                .startObject("simple")
                .startObject("properties")
                .startObject("simpleInteger").field("type", "integer").endObject()
                .startObject("simpleString").field("type", "string").endObject()
                .endObject()
                .field("type", "object").endObject()
                .startObject("simpleAnno")
                .startObject("properties")
                .startObject("originalDate").field("store", true).field("type", "string").endObject()
                .startObject("simpleInteger").field("index_name", "intField").field("type", "integer").endObject()
                .startObject("simpleString").field("index", "no").field("type", "string").endObject()
                .startObject("stringDate").field("format", "yyyy-MM-dd").field("type", "date").endObject()
                .endObject()
                .field("type", "object").endObject()
                .endObject()
                .endObject()
                .endObject();
        assertEquals(mapping.string(), schema.getSource());
    }

    @Test
    public void testCache() {
        EsSchema schema = new EsSchemaBuilder(SimpleBean.class).schema();
        EsSchema schema2 = new EsSchemaBuilder(SimpleBean.class).schema();
        assertSame(schema, schema2);
    }

}
