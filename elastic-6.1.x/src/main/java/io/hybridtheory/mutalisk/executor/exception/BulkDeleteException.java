package io.hybridtheory.mutalisk.executor.exception;


import org.elasticsearch.action.bulk.BulkItemResponse;

import java.util.List;

public class BulkDeleteException extends Exception {
    List<BulkItemResponse.Failure> failures;

    public BulkDeleteException(List<BulkItemResponse.Failure> failures) {
        this.failures = failures;
    }
}
