package io.hybridtheory.mutalisk.common.schema;

// elasticsearch types:
// https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html

public enum ElasticSearchFieldType {
    TEXT,            // with word split
    KEYWORD,        // full text value
    LONG,
    INTEGER,
    SHORT,
    BYTE,
    FLOAT,
    DOUBLE,
    DATE,
    BOOLEAN,
    BINARY,
    ARRAY,
    OBJECT,
    NESTED,
    AUTO,           // auto detect, default value
    NONE            // default value
}
