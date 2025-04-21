package com.zhuo.piper.core.parser;

public interface Parser {
    String parse(String template, Object input) throws Exception;
}
