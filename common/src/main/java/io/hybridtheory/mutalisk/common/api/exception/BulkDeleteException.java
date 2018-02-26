package io.hybridtheory.mutalisk.common.api.exception;


import java.util.List;

public class BulkDeleteException extends Exception {
    List<?> failures;

    public BulkDeleteException(List<?> failures) {
        this.failures = failures;
    }
}
