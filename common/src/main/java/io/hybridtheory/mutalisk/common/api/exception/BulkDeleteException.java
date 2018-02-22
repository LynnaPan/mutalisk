package io.hybridtheory.mutalisk.common.api.exception;


import java.util.List;

public class BulkDeleteException extends Exception {
    List<Object> failures;
    public BulkDeleteException(List<Object> failures) {
        this.failures = failures;
    }
}
