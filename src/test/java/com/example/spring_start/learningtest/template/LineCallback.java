package com.example.spring_start.learningtest.template;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
