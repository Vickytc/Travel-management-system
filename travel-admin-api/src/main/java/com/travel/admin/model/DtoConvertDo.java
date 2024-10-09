package com.travel.admin.model;

/**
 * DTO conversion domain interface
 */
public interface DtoConvertDo<T> {
    /**
     * Provides a method for converting dto into do entity objects
     * @return  T
     */
    T convert();
}
