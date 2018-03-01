package io.hybridtheory.mutalisk.webservice.exception;

public class EntityJsonParseFailException extends Exception {

    public EntityJsonParseFailException(String clz) {
        super("Entity json body is unable to be parsed to " + clz);
    }
}